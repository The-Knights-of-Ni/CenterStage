package org.firstinspires.ftc.teamcode.Subsystems.Vision.ScoringAlgorithm;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.MarkerDetectionPipeline;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.Vision;
import org.firstinspires.ftc.teamcode.Util.PixelColor;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

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

        for(int i = 1; i <= backdrop.rowamount*2 ; i++)
        {
            Pixel foundpixel = new Pixel();
            int countshort = 1;
            int countlong = 1;
            //logic to be determined
            foundpixel.color  = PixelColor.WHITE;
            if (i%2 == 0)
            {
                for(int j=1; j <= backdrop.longlength; j++)
                    backdrop.longRows[countlong][j] = foundpixel;
                countlong++;
            }
            else
            {
                for(int j=1; j<= backdrop.shortlength; j++)
                    backdrop.shortRows[countshort][j] = foundpixel;
                countshort++;
            }
        }

        return input;
    }

}



