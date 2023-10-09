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
    loop {
        let mut frame = Mat::default();
        cam.read(&mut frame)?;
        if frame.size()?.width > 0 {
            highgui::imshow(window, &get_edges_pipeline(&frame)?)?;
        }
        let key = highgui::wait_key(10)?;
        if key > 0 && key != 255 {
            break;
        }
    }
    Ok(())
}