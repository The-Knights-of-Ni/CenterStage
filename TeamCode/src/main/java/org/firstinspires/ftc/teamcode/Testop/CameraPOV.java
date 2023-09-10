package org.firstinspires.ftc.teamcode.Testop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.ConeColorPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

/**
 * This shows what the camera is seeing
 */
@Autonomous(name = "CameraPOV", group = "Concept")
public class CameraPOV extends LinearOpMode {
    public static final int CAMERA_WIDTH = 1920; // width of wanted camera resolution
    public static final int CAMERA_HEIGHT = 1080; // height of wanted camera resolution
    public static final String WEBCAM_NAME = "Webcam 1";
    private OpenCvCamera camera;
    private ConeColorPipeline pipeline;

    private void initCamera() {
        int cameraMonitorViewId =
                hardwareMap
                        .appContext
                        .getResources()
                        .getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        camera =
                OpenCvCameraFactory.getInstance()
                        .createWebcam(hardwareMap.get(WebcamName.class, WEBCAM_NAME), cameraMonitorViewId);
        camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
        // Create a detection pipeline for detecting the position
        pipeline = new ConeColorPipeline(CAMERA_WIDTH, CAMERA_HEIGHT);
        camera.setPipeline(pipeline);
        camera.openCameraDeviceAsync(
                new OpenCvCamera.AsyncCameraOpenListener() {
                    @Override
                    public void onOpened() {
                        telemetry.addLine("Streaming");
                        camera.startStreaming(CAMERA_WIDTH, CAMERA_HEIGHT, OpenCvCameraRotation.UPRIGHT);
                    }

                    @Override
                    public void onError(int errorCode) {
                        telemetry.addLine("Error Streaming, aborting");
                        telemetry.update();
                    }
                });
    }

    @Override
    public void runOpMode() throws InterruptedException {
        initCamera();
        waitForStart();

        while(opModeIsActive()) {
            telemetry.addData("Cone Color", pipeline.getConeColor().color);
            telemetry.update();
        }
    }
}
