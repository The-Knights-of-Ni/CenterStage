package org.firstinspires.ftc.teamcode.Subsystems.Vision.ScoringAlgorithm;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.firstinspires.ftc.teamcode.Util.PixelColor;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.opencv.core.*;

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

    private InMosaicFinder m_inMosaicFinder;
    private MosaicScoreFinder m_mosaicScoreFinder;

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

    public Pixel PixelAlgorithm(Backdrop backdrop, Pixel givenPixel){
        int countshort = 0;
        int countlong = 0;
        for(int i = 1; i <= backdrop.rowamount*2 ; i++)
        {
            if (i%2 == 0)
            {
                for(int j=0; j <= backdrop.longlength-1; j++) {
                    if(j==0)
                    {
                        backdrop.longRows[countlong][j].partofMosaic =
                                m_inMosaicFinder.inMosaic_Case1(backdrop.longRows[countlong][0],
                                        backdrop.shortRows[countshort][0], backdrop.longRows[countlong][1],
                                        backdrop.shortRows[countshort-1][0]);
                    }
                    else if(j==backdrop.longlength-1)
                    {
                       backdrop.longRows[countlong][j].partofMosaic =
                               m_inMosaicFinder.inMosaic_Case1(backdrop.longRows[countlong][backdrop.longlength-1],
                                       backdrop.shortRows[countshort][backdrop.shortlength-1],
                                       backdrop.longRows[countlong][backdrop.longlength-2],
                                       backdrop.shortRows[countshort-1][backdrop.shortlength-1]);
                    }
                    backdrop.longRows[countlong][j].partofMosaic = true;
                    }
                countlong++;

            }
            else
            {
                for(int j=0; j<= backdrop.shortlength-1; j++) {
                    if(countshort==0 && j == 0)
                    {
                        backdrop.shortRows[countshort][j].partofMosaic = m_inMosaicFinder.inMosaic_Case2(
                                backdrop.shortRows[countshort][0], backdrop.longRows[countlong][0],
                                backdrop.longRows[countlong][1], backdrop.shortRows[countshort][1]);
                    }
                    else if (countshort == 0 && j == backdrop.shortlength-1)
                    {
                        backdrop.shortRows[countshort][j].partofMosaic = m_inMosaicFinder.inMosaic_Case2(
                                backdrop.shortRows[countshort][j], backdrop.longRows[countlong][backdrop.longlength-1],
                                backdrop.longRows[countlong][backdrop.longlength-2], backdrop.shortRows[countshort][j-1]);
                    }
                    backdrop.shortRows[countshort][j].partofMosaic = true;
                    }
                countshort++;
            }
        }
        countshort = 0;
        countlong = 0;
        for(int i = 1; i <= backdrop.rowamount*2 ; i++)
        {
            if (i%2 == 0)
            {
                for(int j=0; j <= backdrop.longlength-1; j++) {
                    if(backdrop.longRows[countlong][j].color == PixelColor.Empty) {
                        if (j == 0 && backdrop.shortRows[countshort - 1][j].color != PixelColor.Empty)
                            backdrop.longRows[countlong][j].available = true;
                        else if (j == backdrop.longlength - 1 && backdrop.shortRows[countshort - 1][j - 1].color != PixelColor.Empty)
                            backdrop.longRows[countlong][j].available = true;
                        else if (backdrop.shortRows[countshort - 1][j].color != PixelColor.Empty
                                && backdrop.shortRows[countshort - 1][j - 1].color != PixelColor.Empty)
                            backdrop.longRows[countlong][j].available = true;
                    }
                }
                countlong++;
            }
            else
            {
                for(int j=0; j<= backdrop.shortlength-1; j++) {
                    if(backdrop.shortRows[countshort][j].color == PixelColor.Empty) {
                        if (countshort == 0)
                            backdrop.shortRows[countshort][j].available = true;
                        else if (backdrop.longRows[countlong - 1][j].color != PixelColor.Empty
                                && backdrop.longRows[countlong - 1][j + 1].color != PixelColor.Empty)
                            backdrop.shortRows[countshort][j].available = true;
                    }
                }
                countshort++;
            }
        }

        countshort = 0;
        countlong = 0;
        for(int i = 1; i <= backdrop.rowamount*2 ; i++)
        {
            if (i%2 == 0)
            {
                for(int j=0; j <= backdrop.longlength-1; j++) {
                    if (backdrop.longRows[i][j].available = true)
                    {
                        if(j==0)
                        {

                        }
                    }
                }
                countlong++;
            }
            else
            {
                for(int j=0; j<= backdrop.shortlength-1; j++) {
                    if (backdrop.shortRows[i][j].available = true) {

                    }
                }
                countshort++;
            }
        }



        Pixel bestPixel = new Pixel();
        return bestPixel;
    }
}

