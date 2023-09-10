package org.firstinspires.ftc.teamcode.Subsystems.Vision;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Core;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

/**
 * This pipeline detects where the cone is.
 *
 * <p>It does this by splitting the camera input into 3 parts, the Left, Middle, and Right. It
 * checks each part for a custom marker (which is set to be green in the code), or some blue or red
 * tape, dependant on the alliance color. The marker is assumed to be yellow.</p>
 *
 * @see OpenCvPipeline
 * @see Vision
 */
public class ConeColorPipeline extends OpenCvPipeline {
    private final int CAMERA_WIDTH;
    private final int CAMERA_HEIGHT;
    private ConeColor coneColor = ConeColor.OTHER;


    /**
     * The cone color with the hsv constants
     */
    public enum ConeColor {
        GREEN(new Scalar(50,80,20), new Scalar(90,255,255), "Green"),
        ORANGE(new Scalar(4, 100, 50), new Scalar(25, 255, 255), "Orange"),
        PINK(new Scalar(145,100,50), new Scalar(165,200,255), "Pink"),
        OTHER(new Scalar(0,0,0), new Scalar(0,0,0), "Other"); // leave OTHER as is
        public final Scalar lowHSV;
        public final Scalar highHSV;
        public final String color;
        ConeColor(Scalar lowHSV, Scalar highHSV, String color) { this.highHSV = highHSV; this.lowHSV = lowHSV; this.color = color;}

        @Override
        public String toString() {
            return "ConeColor{" +
                    "lowHSV=" + lowHSV +
                    ", highHSV=" + highHSV +
                    '}';
        }
    }

    /**
     * Class instantiation
     *
     * @see Robot
     * @see Telemetry
     */
    public ConeColorPipeline(int width, int height) {
        this.CAMERA_WIDTH = width;
        this.CAMERA_HEIGHT = height;
    }

    /**
     * This method detects where the marker is.
     *
     *
     * @param input A Mask (the class is called {@link Mat})
     * @return The marker location
     * @see Mat
     * @see Scalar
     * @see ConeColor
     */
    @Override
    public Mat processFrame(Mat input) {
        Mat mask = new Mat();
        Imgproc.cvtColor(input, mask, Imgproc.COLOR_RGB2HSV);

        final int CROP_WIDTH = 480;
        final int CROP_HEIGHT = 400;
        Rect rectCrop = new Rect(720, 324, CROP_WIDTH, CROP_HEIGHT);
        Mat crop = new Mat(mask, rectCrop);

        // Find all pixels within given threshold color values
        Mat threshMagenta = new Mat();
        Mat threshGreen = new Mat();
        Mat threshOrange = new Mat();

        Core.inRange(crop, ConeColor.PINK.lowHSV, ConeColor.PINK.highHSV, threshMagenta);
        Core.inRange(crop, ConeColor.GREEN.lowHSV, ConeColor.GREEN.highHSV, threshGreen);
        Core.inRange(crop, ConeColor.ORANGE.lowHSV, ConeColor.ORANGE.highHSV, threshOrange);

        // Select the mat which has the most white pixels
        double magentaSum = Core.sumElems(threshMagenta).val[0] / (CAMERA_HEIGHT*CROP_WIDTH) / 255;
        double greenSum = Core.sumElems(threshGreen).val[0] / (CAMERA_HEIGHT*CROP_WIDTH) / 255;
        double cyanSum = Core.sumElems(threshOrange).val[0] / (CAMERA_HEIGHT * CROP_WIDTH) / 255;

        if(magentaSum > 0 || greenSum > 0 || cyanSum > 0) {
            if(magentaSum >= greenSum && magentaSum >= cyanSum) {
                coneColor = ConeColor.PINK;
            } else if(greenSum >= cyanSum) {
                coneColor = ConeColor.GREEN;
            } else {
                coneColor = ConeColor.ORANGE;
            }
        } else {
            coneColor = ConeColor.OTHER;
        }

        // Return whichever mat is desired to be viewed on Camera Stream
        return threshMagenta;
    }

    /**
     * Gets the Marker Location, might be not found because of the Search Status.
     *
     * @return Where the marker is.
     * @see ConeColor
     */
    public ConeColor getConeColor() {
        return coneColor;
    }
}