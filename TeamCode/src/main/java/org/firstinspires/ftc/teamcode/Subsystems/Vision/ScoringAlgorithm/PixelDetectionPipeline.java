package org.firstinspires.ftc.teamcode.Subsystems.Vision.ScoringAlgorithm;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PixelDetectionPipeline {
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

    public List<Rect> detectPixelsOnColor(Mat input, Scalar low, Scalar high, String color) {
        Mat threshold = new Mat();
        Core.inRange(input, low, high, threshold);
        String out_file1 = "D:/Documents/Programming/CenterStage/test_resources/output_" + color + ".webp";
        // Writing the image
        Imgcodecs.imwrite(out_file1, threshold);
        Mat edges = new Mat();
        // Apply Canny edge detection
        Imgproc.Canny(threshold, edges, 50, 150, 3, false);

        // Find contours in the image
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(edges, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        Mat labels = Mat.zeros(threshold.size(), CvType.CV_8UC1);
        double[] contour_avgs = new double[contours.size()];
        for (int i = 0; i < contours.size(); i++) {
            Imgproc.drawContours(labels, contours, i, new Scalar(i), Core.FILLED);
            Rect roi = Imgproc.boundingRect(contours.get(i));
            var cropped_threshold = threshold.submat(roi);
            var cropped_labels = labels.submat(roi);
            Scalar mean = Core.mean(cropped_threshold, cropped_labels);
            contour_avgs[i] = mean.val[0];
        }

        // Iterate through the contours to find hexagonal shapes
        List<MatOfPoint> hexagonContours = new ArrayList<>();
        int i = 0;
        for (MatOfPoint contour : contours) {
            MatOfPoint2f approxCurve = new MatOfPoint2f();
            MatOfPoint2f contour2f = new MatOfPoint2f(contour.toArray());

            // Approximate polygonal curves
            double epsilon = 0.02 * Imgproc.arcLength(contour2f, true);
            Imgproc.approxPolyDP(contour2f, approxCurve, epsilon, true);
            Rect boundingRect = Imgproc.boundingRect(contour);

            var area = Imgproc.contourArea(contour);
            var avg_color = contour_avgs[i];
            if (avg_color < 50 && area > 5 && Math.abs(boundingRect.width - boundingRect.height) < Math.min(boundingRect.height, boundingRect.width) / 2) {
                System.out.println(avg_color);
                System.out.println(area);
                System.out.println(approxCurve.total());
                hexagonContours.add(contour);
            }
            i++;
        }
        var rects = new ArrayList<Rect>();
        for (MatOfPoint hexagonContour : hexagonContours) {
            Rect boundingRect = Imgproc.boundingRect(hexagonContour);
            rects.add(boundingRect);
        }
        return rects;
    }

//    @Override
    public Mat processFrame(Mat image) {
        // Apply GaussianBlur to the image to reduce noise
        Imgproc.GaussianBlur(image, image, new Size(5, 5), 0);
        Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2HSV);
        var whiteHexagons = detectPixelsOnColor(image, new Scalar(0, 0, 175), new Scalar(255, 45, 255), "white");
        var purpleHexagons = detectPixelsOnColor(image, new Scalar(150, 35, 60), new Scalar(190, 255, 255), "purple");
        var yellowHexagons = detectPixelsOnColor(image, new Scalar(80, 100, 60), new Scalar(150, 255, 255), "yellow");
        var greenHexagons = detectPixelsOnColor(image, new Scalar(30, 35, 60), new Scalar(80, 255, 255), "green");
        // Draw the rects on the image
        for (Rect rect : whiteHexagons) {
            Imgproc.rectangle(image, rect, new Scalar(255, 255, 255), 2);
        }
        for (Rect rect : greenHexagons) {
            Imgproc.rectangle(image, rect, new Scalar(0, 255, 0), 2);
        }
        for (Rect rect : purpleHexagons) {
            Imgproc.rectangle(image, rect, new Scalar(255, 0, 255), 2);
        }
        for (Rect rect : yellowHexagons) {
            Imgproc.rectangle(image, rect, new Scalar(255, 255, 0), 2);
        }
        return image;
    } // TODO: Test this stuff

    public Backdrop getPixels() {
        return pixels;
    }
}
