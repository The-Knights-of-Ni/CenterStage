package org.firstinspires.ftc.teamcode.Subsystems.Vision.ScoringAlgorithm;

import org.firstinspires.ftc.teamcode.Subsystems.Vision.Vision;
import org.firstinspires.ftc.teamcode.Util.PixelColor;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

/**
 * This pipeline detects where the custom marker is.
 *
 * @see OpenCvPipeline
 * @see Vision
 */


public class PixelDetectionPipeline extends OpenCvPipeline {

    public Backdrop backdrop;
    private final int CAMERA_HEIGHT;
    private final int CAMERA_WIDTH;

    public PixelDetectionPipeline(int height, int width) {
        this.CAMERA_HEIGHT = height;
        this.CAMERA_WIDTH = width;
    }

    public Mat processFrame(Mat input) {

        Mat mask = new Mat();
        Imgproc.cvtColor(input, mask, Imgproc.COLOR_RGB2HSV);

        Rect rectCrop = new Rect(0, 720, 1920, 360);
        Mat crop = new Mat(mask, rectCrop);
        mask.release();


        if (crop.empty()) {
            return input;
        }
        int countshort = 0;
        int countlong = 0;
        for (int i = 1; i <= backdrop.rowamount * 2; i++) {
            Pixel foundPixel = new Pixel();
            // logic to be determined
            foundPixel.color = PixelColor.Empty;
            if (i % 2 == 0) {
                for (int j = 0; j <= backdrop.longlength - 1; j++) {
                    foundPixel.y = countlong*2+1;
                    foundPixel.x = j;
                    backdrop.longRows[countlong][j] = foundPixel;

                }
                countlong++;
            } else {
                for (int j = 0; j <= backdrop.shortlength - 1; j++) {
                    foundPixel.y = countshort*2;
                    foundPixel.x = j;
                    backdrop.shortRows[countshort][j] = foundPixel;
                }
                countshort++;
            }
        }

        return input;
    }

}



