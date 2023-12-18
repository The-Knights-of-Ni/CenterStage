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

        return input;
    }
}



