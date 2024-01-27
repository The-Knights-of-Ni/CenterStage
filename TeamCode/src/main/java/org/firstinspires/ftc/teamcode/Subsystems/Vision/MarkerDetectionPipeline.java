package org.firstinspires.ftc.teamcode.Subsystems.Vision;

import android.util.Log;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
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
public class MarkerDetectionPipeline extends OpenCvPipeline {
    private final AllianceColor allianceColor;
    private final int CAMERA_HEIGHT;
    private final int CAMERA_WIDTH;
    private MarkerLocation markerLocation = MarkerLocation.NOT_FOUND;

    /**
     * Class instantiation
     *
     * @see Telemetry
     * @see AllianceColor
     */
    public MarkerDetectionPipeline(AllianceColor allianceColor, int height, int width) {
        this.allianceColor = allianceColor;
        this.CAMERA_HEIGHT = height;
        this.CAMERA_WIDTH = width;
    }

    /**
     * This method detects where the marker is.
     *
     * <p>It does this by splitting the camera input into left, right, and middle rectangles, these
     * rectangles need to be calibrated. Combined, they do not have to encompass the whole camera
     * input, they probably will only check a small part of it. We then assume the alliance color is
     * either (255, 0, 0) or (0, 0, 255), we get the info when the object is instantiated ({@link
     * #allianceColor}), and that the marker color is (0, 255, 0), which is a bright green ({@link
     * Scalar}'s are used for colors). We compare the marker color with the alliance color on each of
     * the rectangles, if the marker color is on none or multiple of them, it is marked as {@link
     * MarkerLocation#NOT_FOUND}, if otherwise, the respective Location it is in is returned via a
     * {@link MarkerLocation} variable called {@link #markerLocation}
     *
     * @param input A Mask (the class is called {@link Mat})
     * @return The marker location
     * @see #allianceColor
     * @see Mat
     * @see Scalar
     * @see MarkerLocation
     */
    @Override
    public Mat processFrame(Mat input) {
        if (input == null) {
            return null;
        }
        Mat mask = new Mat();
        Imgproc.cvtColor(input, mask, Imgproc.COLOR_RGB2HSV);

//        Rect rectCrop = new Rect(0, 0, 1920, 720);
//        Mat crop = new Mat(mask, rectCrop);
//        mask.release();

        if (mask.empty()) {
            markerLocation = MarkerLocation.NOT_FOUND;
            return input;
        }
        Scalar lowHSV;
        Scalar highHSV;
        // Or red
        if (allianceColor == AllianceColor.RED) {
            lowHSV = new Scalar(100.0, 150.0, 200.0);
            highHSV = new Scalar(125.0, 255.0, 255.0);
        } else {
            // Default to blue
            // Blue alliance
            lowHSV = new Scalar(10.0, 100.0, 70.0);
            highHSV = new Scalar(30.0, 255.0, 255.0);
        }
        Mat thresh = new Mat();

        Core.inRange(mask, lowHSV, highHSV, thresh);
        mask.release();

        Mat edges = new Mat();
        Imgproc.Canny(thresh, edges, 100, 300);
        thresh.release();
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint2f[] contoursPoly = new MatOfPoint2f[contours.size()];
        Rect[] boundRect = new Rect[contours.size()];

        for (int i = 0; i < contours.size(); i++) {
            if (contours.get(i).empty()) {
                Log.w("MarkerDetectionPipeline", "Empty contour");
            } else if (contours.get(i) == null) {
                Log.w("MarkerDetectionPipeline", "Null contour");
            } else {
                MatOfPoint2f tempContours = new MatOfPoint2f(contours.get(i).toArray());
                // IMPORTANT: MatOfPoint2f will prob leak memory, may want to fix
                contoursPoly[i] = new MatOfPoint2f();
                Imgproc.approxPolyDP(tempContours, contoursPoly[i], 3, true);
                MatOfPoint rectContours = new MatOfPoint(contoursPoly[i].toArray());
                boundRect[i] = Imgproc.boundingRect(rectContours);
//            Imgproc.contourArea(contoursPoly[i]); // TODO Maybe implement contour area check for next tourney
                tempContours.release();
                rectContours.release();
            }
        }

        for (int i = 0; i < contours.size(); i++) {
            contours.get(i).release();
            contoursPoly[i].release();
        }

        hierarchy.release();
        edges.release();

        double left_x = 0.375 * CAMERA_WIDTH;
        double right_x = 0.625 * CAMERA_WIDTH;

        var largest_area = 0.0;

        for (int i = 0; i != boundRect.length; i++) {
            if (boundRect[i] != null) {
                double area = boundRect[i].area();
                if (area > largest_area) {
                    largest_area = area;
                    int midpoint = boundRect[i].x + boundRect[i].width / 2;
                    if (midpoint < left_x) {
                        markerLocation = MarkerLocation.LEFT;
                    } else if (left_x <= midpoint && midpoint <= right_x) {
                        markerLocation = MarkerLocation.MIDDLE;
                    } else if (right_x < midpoint) {
                        markerLocation = MarkerLocation.RIGHT;
                    }
                }
            }
        }

        return input;
    }

    /**
     * Gets the Marker Location, might be not found because of the Search Status.
     *
     * @return Where the marker is.
     * @see MarkerLocation
     */
    public MarkerLocation getMarkerLocation() {
        return markerLocation;
    }

    public enum MarkerLocation {
        LEFT, MIDDLE, RIGHT, NOT_FOUND
    }
}