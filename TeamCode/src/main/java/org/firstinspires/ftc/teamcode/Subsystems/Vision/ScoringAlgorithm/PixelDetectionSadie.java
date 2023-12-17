package org.firstinspires.ftc.teamcode.Subsystems.Vision.ScoringAlgorithm;

import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;
import org.opencv.*;
import org.opencv.imgproc.Imgproc;

public class PixelDetectionSadie extends OpenCvPipeline
{
    @Override
    public Mat processFrame(Mat input)
    {
        Mat grayscale = new Mat();
        Imgproc.cvtColor(input, grayscale, Imgproc.COLOR_BGR2GRAY);
        input.release();
        Mat threshold = new Mat();
        Imgproc.threshold(grayscale, threshold, 0, 255, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);


        return null;
    }

    /*public String detectShape(Mat c)
    {

    }*/
}
