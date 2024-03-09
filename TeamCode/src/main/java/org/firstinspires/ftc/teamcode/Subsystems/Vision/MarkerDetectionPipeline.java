package org.firstinspires.ftc.teamcode.Subsystems.Vision;

import android.util.Log;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This pipeline detects where the custom marker is.
 *
 * @see OpenCvPipeline
 * @see Vision
 */
public class MarkerDetectionPipeline extends OpenCvPipeline {
    private final AllianceColor allianceColor;
    private MarkerLocation markerLocation = MarkerLocation.NOT_FOUND;
    private int markerLeftDetected = 0;
    private int markerMiddleDetected = 0;
    private int markerRightDetected = 0;

    /**
     * Class instantiation
     *
     * @see Telemetry
     * @see AllianceColor
     */
    public MarkerDetectionPipeline(AllianceColor allianceColor) {
        this.allianceColor = allianceColor;
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
     * @see MarkerDetectionPipeline#allianceColor
     * @see Mat
     * @see Scalar
     * @see MarkerLocation
     */
    @Override
    public Mat processFrame(Mat input) {
        Log.v("MarkerDetectionPipeline", "Processing frame of size " + input.width() + "x" + input.height());
        var oldMarkerLocation = markerLocation;
        if (input == null) {
            return null;
        }
        Mat mask = new Mat();
        Imgproc.cvtColor(input, mask, (allianceColor == AllianceColor.RED) ? Imgproc.COLOR_BGR2HSV : Imgproc.COLOR_RGB2HSV);

        Rect rectCrop = new Rect(0, 0, mask.width(), mask.height());
        Mat crop = new Mat(mask, rectCrop);
        mask.release();

        if (crop.empty()) {
            markerLocation = MarkerLocation.NOT_FOUND;
            return input;
        }
        Scalar lowHSV;
        Scalar highHSV;
        if (allianceColor == AllianceColor.RED) {
            lowHSV = new Scalar(109.0, 147.0, 156.0);
            highHSV = new Scalar(132.0, 255.0, 255.0);
        } else {
            // Default to blue
            lowHSV = new Scalar(75.0, 150.0, 150.0);
            highHSV = new Scalar(125.0, 255.0, 255.0);
        }
        Mat thresh = new Mat();

        Core.inRange(crop, lowHSV, highHSV, thresh);

        Mat edges = new Mat();
        Imgproc.Canny(thresh, edges, 100, 300);
//        thresh.release();
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

        System.out.println(Arrays.toString(boundRect));

        double left_x = 0.3 * crop.width();
        double right_x = 0.7 * crop.width();
        var largest_area = 0.0;
        double right_area = 0.0;
        double middle_area = 0.0;
        double left_area = 0.0;
        for (int i = 0; i != boundRect.length; i++) {
            if (boundRect[i] != null) {
                double area = boundRect[i].area();
                int midpoint = boundRect[i].x + boundRect[i].width / 2;
                System.out.println(midpoint);
                if (midpoint < left_x) {
                    left_area += area;
                } else if (left_x <= midpoint && midpoint <= right_x) {
                    middle_area += area;
                } else if (right_x < midpoint) {
                    right_area += area;
                }
            }
        }

        if (right_area > largest_area) {
            largest_area = right_area;
            markerLocation = MarkerLocation.RIGHT;
        }
        if (middle_area > largest_area) {
            largest_area = middle_area;
            markerLocation = MarkerLocation.MIDDLE;
        }
        if (left_area > largest_area) {
            markerLocation = MarkerLocation.LEFT;
        }

        switch (markerLocation) {
            case LEFT:
                markerLeftDetected++;
                break;
            case MIDDLE:
                markerMiddleDetected++;
                break;
            case RIGHT:
                markerRightDetected++;
                break;
            default:
                break;
        }

        for (int i = 0; i < contours.size(); i++) {
            contours.get(i).release();
            contoursPoly[i].release();
        }

        hierarchy.release();
        edges.release();
        crop.release();

        if (oldMarkerLocation != markerLocation) {
            Log.i("MarkerDetectionPipeline", "Marker Location: " + markerLocation.name());
        }

        return thresh;
    }

    /**
     * Gets the Marker Location, might be not found because of the Search Status.
     *
     * @return Where the marker is.
     * @see MarkerLocation
     */
    public MarkerLocation getMarkerLocation() {
        MarkerLocation mostDetected = MarkerLocation.NOT_FOUND;
        int mostDetectedCount = 0;
        if (markerLeftDetected > mostDetectedCount) {
            mostDetectedCount = markerLeftDetected;
            mostDetected = MarkerLocation.LEFT;
        }
        if (markerMiddleDetected > mostDetectedCount) {
            mostDetectedCount = markerMiddleDetected;
            mostDetected = MarkerLocation.MIDDLE;
        }
        if (markerRightDetected > mostDetectedCount) {
            mostDetectedCount = markerRightDetected;
            mostDetected = MarkerLocation.RIGHT;
        }

        return mostDetected;
    }

    public enum MarkerLocation {
        LEFT, MIDDLE, RIGHT, NOT_FOUND
    }
}