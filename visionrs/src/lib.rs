use anyhow::{Error, Result};
use jni;
use jni::objects::JClass;
use jni::sys::jbyte;
use jni::JNIEnv;
use opencv::core::{Mat, Point, Point2f, Rect, Scalar, Vector};
use opencv::prelude::*;
use opencv::{imgproc, videoio};
use std::io::ErrorKind;

pub enum MarkerLocation {
    Left,
    Middle,
    Right,
    Unknown,
}

fn get_crop(input: &Mat, camera_width: i32, camera_height: i32) -> Result<Mat> {
    // let y_height = 2 * camera_height / 3;
    // let y_offset = camera_height - y_height;
    let y_height = camera_height;
    let y_offset = 0;
    let rect_crop = Rect::new(0, y_offset, camera_width, y_height);
    Ok(Mat::roi(input, rect_crop)?)
}

pub fn get_edges_pipeline(input: &Mat, camera_width: i32, camera_height: i32) -> Result<Mat> {
    let mut mask: Mat = Mat::default();
    imgproc::cvt_color(input, &mut mask, imgproc::COLOR_RGB2HSV, 0)?;

    let crop: Mat = get_crop(&mask, camera_width, camera_height)?;
    mask.release()?;
    if crop.empty() {
        return Err(Error::from(std::io::Error::new(
            ErrorKind::InvalidInput,
            "Unable to crop image!",
        )));
    }
    let low_hsv = Scalar::new(70.0, 100.0, 100.0, 0.0);
    let high_hsv = Scalar::new(85.0, 255.0, 235.0, 0.0);
    let mut thresh: Mat = Mat::default();

    opencv::core::in_range(&crop, &low_hsv, &high_hsv, &mut thresh)?;

    let mut edges = Mat::default();
    imgproc::canny(&thresh, &mut edges, 100.0, 300.0, 3, false)?;
    thresh.release()?;
    return Ok(edges);
}

fn get_marker_location_pipeline(
    input: Mat,
    camera_width: i32,
    camera_height: i32,
) -> Result<MarkerLocation> {
    let mut edges = get_edges_pipeline(&input, camera_width, camera_height)?;
    let mut contours: Vector<Vector<Point>> = Vector::new();
    imgproc::find_contours(
        &edges,
        &mut contours,
        imgproc::RETR_TREE,
        imgproc::CHAIN_APPROX_SIMPLE,
        Point::new(0, 0),
    )?;
    edges.release()?;
    let mut contours_poly: Vector<Vector<Point2f>> = Vector::new();
    let mut bound_rect: Vec<Rect> = Vec::new(); // TODO: Maybe use Vector instead of Vec

    for i in 0..contours.len() {
        let mut v: Vector<Point2f> = Vector::new();
        imgproc::approx_poly_dp(&contours.get(i)?, &mut v, 3.0, true)?;
        contours_poly.push(v);
        let area = imgproc::contour_area(&contours_poly.get(i)?, false)?;
        println!("Area: {}", area);
        if area > 0.0 { // Zero area = noise
            bound_rect.push(imgproc::bounding_rect(&contours_poly.get(i)?)?);
        }
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
        return Err(Error::from(std::io::Error::new(
            ErrorKind::InvalidInput,
            "Unable to open default camera!",
        )));
    }
    let mut frame = Mat::default();
    camera.read(&mut frame)?;
    get_marker_location_pipeline(
        frame,
        camera.get(videoio::CAP_PROP_FRAME_WIDTH)? as i32,
        camera.get(videoio::CAP_PROP_FRAME_HEIGHT)? as i32,
    )
}

pub fn marker_location_to_int(marker_location: MarkerLocation) -> i8 {
    match marker_location {
        MarkerLocation::Unknown => 3,
        MarkerLocation::Left => 0,
        MarkerLocation::Middle => 1,
        MarkerLocation::Right => 2,
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
    let marker_location_result = get_marker_location();
    // TODO: Throw java exception on error instead of returning -1
    return if let Ok(marker_location) = marker_location_result {
        jbyte::from(marker_location_to_int(marker_location))
    } else {
        jbyte::from(-1)
    }
}

