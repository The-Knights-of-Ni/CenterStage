use anyhow::Result;
use jni;
use jni::objects::{JClass, JString};
use jni::JNIEnv;
use opencv::prelude::*;
use opencv::core::{Mat, Scalar, Rect};

enum MarkerLocation {
    Left,
    Middle,
    Right,
    Unknown
}

fn getMarkerLocation() -> Result<MarkerLocation> {
    let mask = Mat::new();
    Imgproc.cvtColor(input, mask, Imgproc.COLOR_RGB2HSV);

    let rectCrop = new Rect(0, 720, 1920, 360);
    let Mat crop = new Mat(mask, rectCrop);
    mask.release();
        if (crop.empty()) {
            markerLocation = MarkerLocation.NOT_FOUND;
            return input;
        }

        Scalar lowHSV = new Scalar(20, 100, 100);
        Scalar highHSV = new Scalar(30, 255, 255);
        Mat thresh = new Mat();

        Core.inRange(crop, lowHSV, highHSV, thresh);

        Mat edges = new Mat();
        Imgproc.Canny(thresh, edges, 100, 300);
        thresh.release();

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        edges.release();

        MatOfPoint2f[] contoursPoly = new MatOfPoint2f[contours.size()];
        Rect[] boundRect = new Rect[contours.size()];

        for (int i = 0; i < contours.size(); i++) {
            contoursPoly[i] = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);
            boundRect[i] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[i].toArray()));
//            Imgproc.contourArea(contoursPoly[i]); // TODO Maybe implement contour area check for next tourney
        }

        double left_x = 0.375 * CAMERA_WIDTH;
        double right_x = 0.625 * CAMERA_WIDTH;

        boolean left = false;
        boolean middle = false;
        boolean right = false;

        for (int i = 0; i != boundRect.length; i++) {
            int midpoint = boundRect[i].x + boundRect[i].width / 2;
            if (midpoint < left_x)
                left = true;
            if (left_x <= midpoint && midpoint <= right_x)
                middle = true;
            if (right_x < midpoint)
                right = true;
        }
        if (left) markerLocation = MarkerLocation.LEFT;
        if (middle) markerLocation = MarkerLocation.MIDDLE;
        if (right) markerLocation = MarkerLocation.RIGHT;

        return crop;
    return MarkerLocation::Unknown; // TODO: Implement
}

#[no_mangle]
pub extern "system" fn Java_org_knightsofni_visionrs_NativeVision_process<'local>(
    // Notice that this `env` argument is mutable. Any `JNIEnv` API that may
    // allocate new object references will take a mutable reference to the
    // environment.
    mut env: JNIEnv<'local>,
    // this is the class that owns our static method. Not going to be used, but
    // still needs to have an argument slot
    _class: JClass<'local>,
    input: JString<'local>,
) -> JString<'local> {
    // First, we have to get the string out of java. Check out the `strings`
    // module for more info on how this works.
    let input: String = env
        .get_string(&input)
        .expect("Couldn't get java string!")
        .into();

    // Then we have to create a new java string to return. Again, more info
    // in the `strings` module.
    let output = env
        .new_string("test")
        .expect("Couldn't create java string!");
    output
}