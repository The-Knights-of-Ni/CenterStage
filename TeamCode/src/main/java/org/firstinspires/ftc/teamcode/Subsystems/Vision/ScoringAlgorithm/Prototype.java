package org.firstinspires.ftc.teamcode.Subsystems.Vision.ScoringAlgorithm;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.AprilTagDetectionThread;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.MarkerDetectionPipeline;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.ScoringAlgorithm.PixelDetectionPipeline;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.Vector;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.Vision;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class Prototype extends Subsystem {

    public static final int CAMERA_WIDTH = 1920; // width of wanted camera resolution
    public static final int CAMERA_HEIGHT = 1080; // height of wanted camera resolution
    public static final String WEBCAM_NAME =
            "Webcam 1"; // insert webcam name from configuration if using webcam
    // Since ImageTarget trackable use mm to specify their dimensions, we must use mm for all the
    // physical dimension.
    // Define constants
    private static final double mmPerInch = 25.4;
    // Define where camera is in relation to center of robot in inches
    final static double CAMERA_FORWARD_DISPLACEMENT = 6.0f * mmPerInch; // TODO: CALIBRATE WHEN ROBOT IS BUILT
    final static double CAMERA_VERTICAL_DISPLACEMENT = 6.5f * mmPerInch;
    final static double CAMERA_RIGHT_DISPLACEMENT = 0.0f * mmPerInch;
    private final HardwareMap hardwareMap;

    Scalar White = new Scalar(100, 0, 0);

    private OpenCvCamera camera;

    private PixelDetectionPipeline pipeline;

    public Prototype(
            Telemetry telemetry,
            HardwareMap hardwareMap,
            AllianceColor allianceColor) {
        super(telemetry, "vision");
        this.hardwareMap = hardwareMap;
        // Telemetry
        logger.info("Vision init complete");
    }

    private void initDetectionPipeline() {
        // Get the camera ID
        int cameraMonitorViewId =
                hardwareMap
                        .appContext
                        .getResources()
                        .getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        // Obtain camera instance from ID
        camera =
                OpenCvCameraFactory.getInstance()
                        .createWebcam(hardwareMap.get(WebcamName.class, WEBCAM_NAME), cameraMonitorViewId);

        // Create a detection pipeline for detecting the position
        pipeline = new PixelDetectionPipeline(CAMERA_HEIGHT, CAMERA_WIDTH);
        camera.setPipeline(pipeline);

        // Create listeners for the camera
        camera.openCameraDeviceAsync(
                new OpenCvCamera.AsyncCameraOpenListener() {
                    @Override
                    public void onOpened() { // Listener for when the camera first starts
                        logger.info("Streaming");
                        camera.startStreaming(CAMERA_WIDTH, CAMERA_HEIGHT, OpenCvCameraRotation.UPRIGHT);
                    }

                    @Override
                    public void onError(int errorCode) { // Listener to log if the camera stops abruptly
                        logger.error("Error Streaming, aborting with error code " + errorCode);
                    }
                });
    }

    public void stop() {
        // Stop streaming
        camera.stopStreaming();
        camera.closeCameraDevice();
    }

    /**
     * This method waits until the search for the marker is done, and then it return the marker
     * location. It waits until the marker is found, then it returns the marker location.
     *
     * @return Where the marker is
     */
    public Backdrop getBackdrop() {
        // Return the marker location
        return pipeline.backdrop;
    }

    public Pixel PixelAlgorithm(){
        Pixel bestPixel = new Pixel();
        return bestPixel;
    }
}

