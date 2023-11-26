use anyhow::{Error, Result};
use jni;
use jni::objects::JClass;
use jni::sys::jbyte;
use jni::JNIEnv;
use opencv::core::{Mat, Point, Point2f, Rect, Scalar, Vector};
use opencv::prelude::*;
use opencv::{imgproc, videoio};
use std::io::ErrorKind;

#[derive(Copy, Clone, Debug, PartialEq)]
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
    return Ok(edges);
}

pub fn get_marker_location_pipeline(
    input: Mat,
    camera_width: i32,
    camera_height: i32,
) -> Result<MarkerLocation> {
    let edges = get_edges_pipeline(&input, camera_width, camera_height)?;
    let mut contours: Vector<Vector<Point>> = Vector::new();
    imgproc::find_contours(
        &edges,
        &mut contours,
        imgproc::RETR_TREE,
        imgproc::CHAIN_APPROX_SIMPLE,
        Point::new(0, 0),
    )?;
    let mut contours_poly: Vector<Vector<Point2f>> = Vector::new();
    let mut bound_rect: Vec<Rect> = Vec::new();
    let mut contour_areas: Vec<f64> = Vec::new();
    for i in 0..contours.len() {
        let mut v: Vector<Point2f> = Vector::new();
        imgproc::approx_poly_dp(&contours.get(i)?, &mut v, 3.0, true)?;
        contours_poly.push(v);
        let area = imgproc::contour_area(&contours_poly.get(i)?, false)?;
        if area > 0.0 {
            // Zero area = noise
            bound_rect.push(imgproc::bounding_rect(&contours_poly.get(i)?)?);
            contour_areas.push(area);
        }
    }
    let left_x = (0.375 * camera_width as f64) as i32;
    let right_x = (0.625 * camera_width as f64) as i32;

    let mut left = 0.0;
    let mut middle = 0.0;
    let mut right = 0.0;

    for (count, rect) in bound_rect.iter().enumerate() {
        let midpoint = rect.x + rect.width / 2;
        let area = contour_areas
            .get(count)
            .ok_or(Error::from(std::io::Error::new(
                ErrorKind::InvalidInput,
                "Unable to get contour area!",
            )))?; // TODO: fix error (shouldn't be io)
        if midpoint < left_x {
            left += area;
        } else if midpoint < right_x {
            middle += area;
        } else {
            right += area;
        }
    }
    if left > middle && left > right {
        Ok(MarkerLocation::Left)
    } else if middle > left && middle > right {
        Ok(MarkerLocation::Middle)
    } else if right > left && right > middle {
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

pub fn marker_location_to_byte(marker_location: MarkerLocation) -> i8 {
    match marker_location {
        MarkerLocation::Unknown => 3,
        MarkerLocation::Left => 0,
        MarkerLocation::Middle => 1,
        MarkerLocation::Right => 2,
    }
}

fn throw_no_class_def_error(env: &mut JNIEnv, message: &str) -> jbyte {
    let class_name = "java/lang/NoClassDefFoundError";
    let ex_class: JClass;

    match env.find_class(class_name) {
        Ok(class) => ex_class = class,
        Err(_) => return -1,
    }

    match env.throw_new(ex_class, message) {
        Ok(_) => -1,
        Err(e) => panic!("Exception occurred {:?}", e), // Can't throw anything else
    }
}

pub fn throw_exception(env: &mut JNIEnv, message: &str) -> jbyte {
    let class_name = "java/lang/Exception";
    let ex_class_r = env.find_class(class_name);
    let ex_class = match ex_class_r {
        Ok(class) => class,
        Err(_) => return throw_no_class_def_error(env, class_name),
    };
    match env.throw_new(ex_class, message) {
        Ok(_) => -1,
        Err(e) => panic!("Exception occurred {:?}", e),
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
) -> jbyte {
    let marker_location_result = get_marker_location();
    match marker_location_result {
        Ok(marker_location) => marker_location_to_byte(marker_location),
        Err(e) => {
            let s = format!("Error: {:?}", e);
            throw_exception(&mut env, &s)
        }
    }
}
