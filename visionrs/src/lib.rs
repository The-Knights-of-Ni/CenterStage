use anyhow::Result;
use jni;
use jni::objects::{JClass, JString};
use jni::JNIEnv;
use opencv::prelude::*;
use opencv::core::{Mat, Scalar, Rect, Point, Vector, Point2f};
use opencv::imgproc;

enum MarkerLocation {
    Left,
    Middle,
    Right,
    Unknown
}

fn getMarkerLocation(input: Mat, CAMERA_WIDTH: i64) -> Result<MarkerLocation> {
    let mut mask: Mat = Mat::new();
    imgproc::cvt_color(&input, &mut mask, imgproc::COLOR_RGB2HSV, 0)?;

    let rect_crop = Rect::new(0, 720, 1920, 360);
    let crop: Mat = Mat::new(mask, rect_crop);
    mask.release()?;
    if crop.empty() {
        return Ok(MarkerLocation::Unknown);
    }
    let low_hsv = Scalar::new(20.0, 100.0, 100.0);
    let high_hsv = Scalar::new(30.0, 255.0, 255.0);
    let mut thresh: Mat = Mat::new();

    opencv::core::in_range(&crop, &low_hsv, &high_hsv, &mut thresh)?;

    let edges = Mat::new();
    imgproc::canny(&thresh, edges, 100.0, 300.0, 3, false)?;
    thresh.release()?;
    let mut contours: Vec<Vector<Point>> = Vec::new();
    imgproc::find_contours(edges, &mut contours, imgproc::RETR_TREE, imgproc::CHAIN_APPROX_SIMPLE, Point::new(0, 0))?;
    edges.release();
    let mut contoursPoly: Vector<Point2f> = Vector::new();
    let mut bound_rect: Vec<Rect> = Vec::new();

    for i in 0.. contours.len() {
        contoursPoly[i] = Vector::new();
        imgproc::approx_poly_dp(&contours[i], contoursPoly[i], 3.0, true)?;
        bound_rect[i] = imgproc::bounding_rect(&Vector::from(contoursPoly[i]))?;
//            Imgproc.contourArea(contoursPoly[i]); // TODO Maybe implement contour area check for next tourney
    }

    let left_x = 0.375 * CAMERA_WIDTH;
    let right_x = 0.625 * CAMERA_WIDTH;

    let mut left = false;
    let mut middle = false;
    let mut right = false;

    for i in 0..bound_rect.len() {
        let midpoint = bound_rect[i].x + bound_rect[i].width / 2;
        if midpoint < left_x {
            left = true;
        }
        if left_x <= midpoint && midpoint <= right_x {
            middle = true;
        }
        if right_x < midpoint {
            right = true;
        }
    }

    if left {
        return Ok(MarkerLocation::Left);
    } else if middle {
        return Ok(MarkerLocation::Middle);
    } else if right {
        return Ok(MarkerLocation::Right);
    } else {
        return Ok(MarkerLocation::Unknown);
    }
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