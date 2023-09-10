package org.firstinspires.ftc.teamcode.Subsystems.Vision;


import org.apache.commons.geometry.euclidean.twod.ConvexArea;
import org.apache.commons.geometry.euclidean.twod.Vector2D;
import org.apache.commons.geometry.euclidean.twod.path.LinePath;

import org.apache.commons.numbers.core.Precision;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraException;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureRequest;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSession;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCharacteristics;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraManager;
import org.firstinspires.ftc.robotcore.internal.collections.EvictingBlockingQueue;
import org.firstinspires.ftc.robotcore.internal.network.CallbackLooper;
import org.firstinspires.ftc.robotcore.internal.system.ContinuationSynchronizer;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import org.firstinspires.ftc.teamcode.DriveControl.BoundingBox;
import org.firstinspires.ftc.teamcode.Util.ThreadExceptionHandler;

import org.tensorflow.lite.Interpreter;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;


public class VisionCorrectionThread implements Runnable {
    private final VisionCorrectionThreadData vtd = VisionCorrectionThreadData.getVTD();
    private Interpreter modelInterpreter;
    ReadWriteLock lock = new ReentrantReadWriteLock();

    private static final String TAG = "Vision Correction";

    /** How long we are to wait to be granted permission to use the camera before giving up. Here,
     * we wait indefinitely */
    private static final int secondsPermissionTimeout = Integer.MAX_VALUE;

    /** State regarding our interaction with the camera */
    private final CameraManager cameraManager;
    private final WebcamName cameraName;
    private Camera camera;
    private CameraCaptureSession cameraCaptureSession;

    /** The queue into which all frames from the camera are placed as they become available.
     * Frames which are not processed by the OpMode are automatically discarded. */
    private EvictingBlockingQueue<Bitmap> frameQueue;

    /** A utility object that indicates where the asynchronous callbacks from the camera
     * infrastructure are to run. In this OpMode, that's all hidden from you (but see {@link #startCamera}
     * if you're curious): no knowledge of multi-threading is needed here. */
    private final Handler callbackHandler;
    public VisionCorrectionThread(WebcamName cameraName) {
        callbackHandler = CallbackLooper.getDefault().getHandler();

        cameraManager = ClassFactory.getInstance().getCameraManager();
        this.cameraName = cameraName;

        initializeFrameQueue(2);
        File captureDirectory = AppUtil.ROBOT_DATA_DIR;
        AppUtil.getInstance().ensureDirectoryExists(captureDirectory);
        openCamera();
        if (camera == null) return;

        startCamera();
        if (cameraCaptureSession == null) return;
        modelInterpreter = new Interpreter(new File(""));
    }


    //----------------------------------------------------------------------------------------------
    // Camera operations
    //----------------------------------------------------------------------------------------------

    private void initializeFrameQueue(int capacity) {
        /* The frame queue will automatically throw away bitmap frames if they are not processed
         * quickly by the OpMode. This avoids a buildup of frames in memory */
        frameQueue = new EvictingBlockingQueue<>(new ArrayBlockingQueue<>(capacity));
        // not strictly necessary, but helpful
        frameQueue.setEvictAction(Bitmap::recycle);
    }

    private void openCamera() {
        Deadline deadline = new Deadline(secondsPermissionTimeout, TimeUnit.SECONDS);
        camera = cameraManager.requestPermissionAndOpenCamera(deadline, cameraName, null);
        if (camera == null) {
            throw new RuntimeException("camera not found or permission to use not granted: " + cameraName);
        }
    }

    private void startCamera() {
        if (cameraCaptureSession != null) return; // be idempotent

        /* YUY2 is supported by all Webcams, per the USB Webcam standard */
        final int imageFormat = ImageFormat.YUY2;

        /* Verify that the image is supported, and fetch size and desired frame rate if so */
        CameraCharacteristics cameraCharacteristics = cameraName.getCameraCharacteristics();
        if (!contains(cameraCharacteristics.getAndroidFormats(), imageFormat)) {
            throw new UnsupportedOperationException("image format not supported");
        }
        final Size size = cameraCharacteristics.getDefaultSize(imageFormat);
        final int fps = cameraCharacteristics.getMaxFramesPerSecond(imageFormat, size);

        /* Parts of the code below runs asynchronously on other threads. Use of the synchronizer
         * here allows us to wait in this method until all the asynchronous stuff completes before returning. */
        final ContinuationSynchronizer<CameraCaptureSession> synchronizer = new ContinuationSynchronizer<>();
        try {
            /* Create a session in which requests to capture frames can be made */
            camera.createCaptureSession(Continuation.create(callbackHandler, new CameraCaptureSession.StateCallbackDefault() {
                @Override public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        /* The session is ready to go. Start requesting frames */
                        final CameraCaptureRequest captureRequest = camera.createCaptureRequest(imageFormat, size, fps);
                        session.startCapture(captureRequest,
                                (session1, request, cameraFrame) -> {
                                    /* A new frame is available. The frame data has <em>not</em> been copied for us, and we can only access it
                                     * for the duration of the callback. So we copy here manually. */
                                    Bitmap bmp = captureRequest.createEmptyBitmap();
                                    cameraFrame.copyToBitmap(bmp);
                                    frameQueue.offer(bmp);
                                },
                                Continuation.create(callbackHandler, (session12, cameraCaptureSequenceId, lastFrameNumber) -> Log.i(TAG, "capture sequence " + cameraCaptureSequenceId + " reports completed: lastFrame=" + lastFrameNumber))
                        );
                        synchronizer.finish(session);
                    } catch (CameraException|RuntimeException e) {
                        Log.e(TAG, "exception starting capture", e);
                        session.close();
                        synchronizer.finish(null);
                    }
                }
            }));
        } catch (CameraException | RuntimeException e) {
            Log.e(TAG, "exception starting camera", e);
            synchronizer.finish(null);
        }

        /* Wait for the synchronizer to complete */
        try {
            synchronizer.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        /* Retrieve the created session. This will be null on error. */
        cameraCaptureSession = synchronizer.getValue();
    }

    private void stopCamera() {
        if (cameraCaptureSession != null) {
            cameraCaptureSession.stopCapture();
            cameraCaptureSession.close();
            cameraCaptureSession = null;
        }
    }

    private void closeCamera() {
        stopCamera();
        if (camera != null) {
            camera.close();
            camera = null;
        }
    }

    private boolean contains(int[] array, int value) {
        for (int i : array) {
            if (i == value) return true;
        }
        return false;
    }

    @Override
    public void run() {
        while (!vtd.getClose()) {
            Thread.currentThread().setUncaughtExceptionHandler(new ThreadExceptionHandler());
            if (lock.writeLock().tryLock()) {
                try {
                    vtd.setCorrectionVector(getBoundingBoxFromModel().distanceFrom(vtd.getTheoreticalPosition()));
                } catch (IOException e) {
                    Log.e(TAG, "Image Error", e);
                } finally {
                    lock.writeLock().unlock();
                }
            } else {
                Log.e(TAG, "unable to acquire lock");
            }
        }
        closeCamera();
        Thread.currentThread().interrupt();
    }

    private BoundingBox getBoundingBoxFromModel() throws IOException {
        Precision.DoubleEquivalence precision = Precision.doubleEquivalenceOfEpsilon(1e-6);
        double distanceOff = 0;
        Map<String, Object> inputs = new HashMap<>();
        Bitmap bmp = frameQueue.poll();
        if (bmp == null) {
            throw new IOException("Image not found");
        }
        int[] input1 = new int[bmp.getHeight()*bmp.getWidth()];
        bmp.getPixels(input1, 0, 0, 0,0, bmp.getHeight(), bmp.getWidth());
        bmp.recycle(); // not strictly necessary, but helpful
        inputs.put("input_1", input1);
        inputs.put("input_2", new int[] {bmp.getHeight(), bmp.getWidth()});
        Map<String, Object> outputs = new HashMap<>();
        outputs.put("output_1", distanceOff);
        modelInterpreter.run(inputs, outputs);
        // create a connected sequence of line segments forming the unit square
        LinePath path = LinePath.builder(precision)
                .append(Vector2D.ZERO)
                .append(Vector2D.Unit.PLUS_X)
                .append(Vector2D.of(1, 1))
                .append(Vector2D.Unit.PLUS_Y)
                .build(true); // build the path, ending it with the starting point
        ConvexArea shape = ConvexArea.convexPolygonFromPath(path);
        return new BoundingBox(shape); //TODO: Add code for obtaining usable bounding boxes from TF model, delete this filler when complete
    }
}
