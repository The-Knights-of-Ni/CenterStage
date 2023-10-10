use std::io::ErrorKind;
use anyhow::{Error, Result};
use jni;
use jni::objects::JClass;
use jni::JNIEnv;
use jni::sys::jbyte;
use opencv::prelude::*;
use opencv::core::{Mat, Scalar, Rect, Point, Vector, Point2f};
use opencv::{imgproc, videoio};

pub enum MarkerLocation {
    Left,
    Middle,
    Right,
    Unknown,
}

fn get_crop(input: &Mat) -> Result<Mat> {
    if input.cols() < 1920 {
        return Ok(input.clone());
    } else {
        let rect_crop = Rect::new(0, 720, 1920, 360);
        Ok(Mat::roi(input, rect_crop)?)
    }
}

pub fn get_edges_pipeline(input: &Mat) -> Result<Mat> {
    let mut mask: Mat = Mat::default();
    imgproc::cvt_color(input, &mut mask, imgproc::COLOR_RGB2HSV, 0)?;

    let crop: Mat = get_crop(&mask)?;
    mask.release()?;
    if crop.empty() {
        return Err(Error::from(std::io::Error::new(ErrorKind::InvalidInput, "Unable to crop image!")));
    }
    let low_hsv = Scalar::new(85.0, 100.0, 100.0, 0.0);
    let high_hsv = Scalar::new(95.0, 255.0, 235.0, 0.0);
    let mut thresh: Mat = Mat::default();

    opencv::core::in_range(&crop, &low_hsv, &high_hsv, &mut thresh)?;

    let mut edges = Mat::default();
    imgproc::canny(&thresh, &mut edges, 100.0, 300.0, 3, false)?;
    thresh.release()?;
    return Ok(edges);
}

fn get_marker_location_pipeline(input: Mat, camera_width: i64) -> Result<MarkerLocation> {
    let mut edges = get_edges_pipeline(&input)?;
    let mut contours: Vector<Vector<Point>> = Vector::new();
    imgproc::find_contours(&edges, &mut contours, imgproc::RETR_TREE, imgproc::CHAIN_APPROX_SIMPLE, Point::new(0, 0))?;
    edges.release()?;
    let mut contours_poly: Vector<Vector<Point2f>> = Vector::new();
    let mut bound_rect: Vec<Rect> = Vec::new(); // TODO: Maybe use Vector instead of Vec

    for i in 0..contours.len() {
        let mut v: Vector<Point2f> = Vector::new();;
        imgproc::approx_poly_dp(&contours.get(i)?, &mut v, 3.0, true)?;
        contours_poly.push(v);
        bound_rect.push(imgproc::bounding_rect(&contours_poly.get(i)?)?);
        // let _area = imgproc::contour_area(&contours_poly.get(i)?, false)?;
        // TODO: Maybe implement contour area check
    }

    let left_x = (0.375 * camera_width as f64) as i32;
    let right_x = (0.625 * camera_width as f64) as i32;

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
        Ok(MarkerLocation::Left)
    } else if middle {
        Ok(MarkerLocation::Middle)
    } else if right {
        Ok(MarkerLocation::Right)
    } else {
        Ok(MarkerLocation::Unknown)
    }
}

pub fn get_marker_location() -> Result<MarkerLocation> {
    let mut camera = videoio::VideoCapture::new(0, videoio::CAP_ANY)?;
    let opened = videoio::VideoCapture::is_opened(&camera)?;
    if !opened {
        return Err(Error::from(std::io::Error::new(ErrorKind::InvalidInput, "Unable to open default camera!")));
    }
    let mut frame = Mat::default();
    camera.read(&mut frame)?;
    get_marker_location_pipeline(frame, camera.get(videoio::CAP_PROP_FRAME_WIDTH)? as i64)
}

pub fn marker_location_to_int(marker_location: MarkerLocation) -> i8 {
    match marker_location {
        MarkerLocation::Unknown => 3,
        MarkerLocation::Left => 0,
        MarkerLocation::Middle => 1,
        MarkerLocation::Right => 2
    }
}

#[no_mangle]
pub extern "system" fn Java_org_knightsofni_visionrs_NativeVision_process<'local>(
    // Notice that this `env` argument is mutable. Any `JNIEnv` API that may
    // allocate new object references will take a mutable reference to the
    // environment.
    mut _env: JNIEnv<'local>,
    // this is the class that owns our static method. Not going to be used, but
    // still needs to have an argument slot
    _class: JClass<'local>,
) -> jbyte {
    // TODO: Throw java exception on error instead of panicking
    jbyte::from(marker_location_to_int(get_marker_location().unwrap()))
}