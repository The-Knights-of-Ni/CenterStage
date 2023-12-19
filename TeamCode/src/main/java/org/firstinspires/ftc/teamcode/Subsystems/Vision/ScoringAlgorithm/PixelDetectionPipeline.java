package org.firstinspires.ftc.teamcode.Subsystems.Vision.ScoringAlgorithm;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.opencv.core.Mat;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

public class PixelDetectionPipeline extends OpenCvPipeline {
    private final int CAMERA_HEIGHT;
    private final int CAMERA_WIDTH;
    private Backdrop pixels = new Backdrop();

    /**
     * Class instantiation
     *
     * @see Robot
     * @see Telemetry
     * @see AllianceColor
     */
    public PixelDetectionPipeline(int height, int width) {
        this.CAMERA_HEIGHT = height;
        this.CAMERA_WIDTH = width;
    }


    @Override
    public Mat processFrame(Mat image) {
        // Apply GaussianBlur to the image to reduce noise
        Imgproc.GaussianBlur(image, image, new Size(5, 5), 0);

        // Apply Canny edge detection
        Mat edges = new Mat();
        Imgproc.Canny(image, edges, 50, 150, 3, false);

        // Find contours in the image
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(edges, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Iterate through the contours to find hexagonal shapes
        List<MatOfPoint> hexagonContours = new ArrayList<>();
        for (MatOfPoint contour : contours) {
            MatOfPoint2f approxCurve = new MatOfPoint2f();
            MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());

            // Approximate polygonal curves
            double epsilon = 0.02 * Imgproc.arcLength(contour2f, true);
            Imgproc.approxPolyDP(contour2f, approxCurve, epsilon, true);

            // Check if the contour has 6 vertices (hexagon)
            if (approxCurve.total() == 6) {
                hexagonContours.add(contour);
            }
        }

        // Iterate through hexagonal contours to find colored hexagons
        for (MatOfPoint hexagonContour : hexagonContours) {
            // Draw the contour on the original image
            Imgproc.drawContours(image, List.of(hexagonContour), -1, new Scalar(255), 2);

            // Calculate the center and color of the hexagon
            Moments moments = Imgproc.moments(hexagonContour);
            int centerX = (int) (moments.m10 / moments.m00);
            int centerY = (int) (moments.m01 / moments.m00);

            // Get the color of the center pixel
            double[] centerPixel = image.get(centerY, centerX);

            // Determine the color based on pixel intensity
            String color = getColor(centerPixel);

            // Print the color and position of each element
            System.out.println("Color: " + color + ", Position: (" + centerX + ", " + centerY + ")"); // TODO: Convert positions to stored value
        }
    } // TODO: Test this stuff

    private static String getColor(double[] pixel) {
        // Assuming pixel intensity values for white, purple, green, and yellow
        double whiteThreshold = 200;
        double purpleThreshold = 150;
        double greenThreshold = 100;
        double yellowThreshold = 50;

        if (pixel[0] > whiteThreshold && pixel[1] > whiteThreshold && pixel[2] > whiteThreshold) {
            return "White";
        } else if (pixel[0] > purpleThreshold && pixel[1] < greenThreshold && pixel[2] > purpleThreshold) {
            return "Purple";
        } else if (pixel[0] < greenThreshold && pixel[1] > greenThreshold && pixel[2] < greenThreshold) {
            return "Green";
        } else if (pixel[0] > yellowThreshold && pixel[1] > yellowThreshold && pixel[2] < yellowThreshold) {
            return "Yellow";
        } else {
            return "Unknown";
        }
    }
}
