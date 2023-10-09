use opencv::{highgui, videoio};
use opencv::core::Mat;
use opencv::prelude::*;
use visionrs::get_edges_pipeline;

fn main() -> anyhow::Result<()> {
    let window = "video capture";
    highgui::named_window(window, highgui::WINDOW_AUTOSIZE)?;
    let mut cam = videoio::VideoCapture::new(0, videoio::CAP_ANY)?; // 0 is the default camera
    let opened = videoio::VideoCapture::is_opened(&cam)?;
    if !opened {
        panic!("Unable to open default camera!");
    }
    println!("Press the A key to toggle the pipeline. Press any other key to exit.");
    let mut perform_pipeline = true;
    loop {
        let mut frame = Mat::default();
        cam.read(&mut frame)?;
        if frame.size()?.width > 0 {
            if perform_pipeline {
                highgui::imshow(window, &get_edges_pipeline(&frame)?)?;
            } else {
                highgui::imshow(window, &frame)?;
            }
        }
        let key = highgui::wait_key(10)?;
        if key == 97 { // "a" key
            perform_pipeline = !perform_pipeline;
        } else if key > 0 && key != 255 {
            break;
        }
    }
    Ok(())
}