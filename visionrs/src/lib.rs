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
    Unknown,
}

fn safe_mat_from_mat_size(mat: &Mat) -> Result<Mat> {
    unsafe {
        let size = mat.size()?;
        let new_mat = Mat::new_size(size, imgproc::COLOR_RGB2HSV)?;
        return Ok(new_mat);
    }
}

fn getMarkerLocation(input: Mat, CAMERA_WIDTH: i64) -> Result<MarkerLocation> {
    let mut mask: Mat = safe_mat_from_mat_size(&input)?;
    imgproc::cvt_color(&input, &mut mask, imgproc::COLOR_RGB2HSV, 0)?;

    let rect_crop = Rect::new(0, 720, 1920, 360);
    let crop: Mat = Mat::new(mask, rect_crop.y, rect_crop.y + rect_crop.height, rect_crop.x, rect_crop.x + rect_crop.width);
    mask.release()?;
    if crop.empty() {
        return Ok(MarkerLocation::Unknown);
    }
    let low_hsv = Scalar::new(20.0, 100.0, 100.0, 0.0);
    let high_hsv = Scalar::new(30.0, 255.0, 255.0, 0.0);
    let mut thresh: Mat = safe_mat_from_mat_size(&crop)?;

    opencv::core::in_range(&crop, &low_hsv, &high_hsv, &mut thresh)?;

    let mut edges = safe_mat_from_mat_size(&thresh)?; // TODO: Check if this is the right way to do this
    imgproc::canny(&thresh, &mut edges, 100.0, 300.0, 3, false)?;
    thresh.release()?;
    let mut contours: Vector<Vector<Point>> = Vector::new();
    imgproc::find_contours(&edges, &mut contours, imgproc::RETR_TREE, imgproc::CHAIN_APPROX_SIMPLE, Point::new(0, 0))?;
    edges.release();
    let mut contours_poly: Vector<Vector<Point2f>> = Vector::new();
    let mut bound_rect: Vec<Rect> = Vec::new();

    for i in 0..contours.len() {
        contours_poly.set(i, Vector::new())?;
        imgproc::approx_poly_dp(&contours.get(i)?, &mut contours_poly.get(i)?, 3.0, true)?;
        bound_rect[i] = imgproc::bounding_rect(&contours_poly.get(i)?)?;
//            Imgproc.contourArea(contours_poly[i]); // TODO Maybe implement contour area check for next tourney
    }

    let left_x = (0.375 * CAMERA_WIDTH as f64) as i32;
    let right_x = (0.625 * CAMERA_WIDTH as f64) as i32;

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
) -> JString<'local> {
    let output = env // TODO: Actually return proper result
        .new_string("unknown")
        .expect("Couldn't create java string!");
    output
}