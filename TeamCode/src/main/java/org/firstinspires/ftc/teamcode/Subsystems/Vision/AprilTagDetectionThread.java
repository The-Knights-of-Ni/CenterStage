package org.firstinspires.ftc.teamcode.Subsystems.Vision;

import android.util.Size;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class AprilTagDetectionThread extends Thread {
    public boolean terminate = false;
    public List<AprilTagDetection> currentDetections;
    /**
     * The variable to store our instance of the AprilTag processor.
     */
    private AprilTagProcessor aprilTag;
    private final CameraName cameraName;
    /**
     * The variable to store our instance of the vision portal.
     */
    private VisionPortal visionPortal;

    public AprilTagDetectionThread(CameraName cameraName) {
        this.cameraName = cameraName;
        initAprilTag();
    }

    public void run() {
        while (!terminate) {
            updateAprilTagList();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Initialize the AprilTag processor.
     */
    private void initAprilTag() {

        // Create the AprilTag processor.
        aprilTag = new AprilTagProcessor.Builder()
                //.setDrawAxes(false)
                //.setDrawCubeProjection(false)
                //.setDrawTagOutline(true)
                //.setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                //.setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                //.setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)

                // == CAMERA CALIBRATION ==
                // If you do not manually specify calibration parameters, the SDK will attempt
                // to load a predefined calibration for your camera.
                //.setLensIntrinsics(578.272, 578.272, 402.145, 221.506)

                // ... these parameters are fx, fy, cx, cy.

                .build();

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(cameraName);
        // Choose a camera resolution. Not all cameras support all resolutions.
        builder.setCameraResolution(new Size(Vision.CAMERA_WIDTH, Vision.CAMERA_HEIGHT));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableCameraMonitoring(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether LiveView stops if no processors are enabled.
        // If set "true", the monitor shows a solid orange screen if no processors enabled.
        // If set "false", the monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(aprilTag);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Disable or re-enable the aprilTag processor at any time.
        visionPortal.setProcessorEnabled(aprilTag, true);
    }


    /**
     * Add telemetry about AprilTag detections.
     */
    private void updateAprilTagList() {
        currentDetections = aprilTag.getDetections();
    }
}
