use opencv::{highgui, videoio};
use opencv::core::Mat;
use opencv::prelude::*;
use visionrs::{get_edges_pipeline, get_marker_location_pipeline, marker_location_to_byte};

fn main() -> anyhow::Result<()> {
    let window = "Video capture/edge detection";
    highgui::named_window(window, highgui::WINDOW_AUTOSIZE)?; // This is sort of slow
    // This line is really slow ... it's why the window opening takes so long
    let mut camera = videoio::VideoCapture::new(0, videoio::CAP_ANY)?; // 0 is the default camera
    let opened = videoio::VideoCapture::is_opened(&camera)?;
    assert!(opened, "Unable to open default camera!");
    println!("Press the A key to toggle the pipeline. Press Q to exit. Press E to run pipeline.");
    let mut perform_pipeline = true;
    loop {
        let mut frame = Mat::default();
        camera.read(&mut frame)?;
        if frame.size()?.width > 0 {
            let mat = if perform_pipeline {
                #[allow(clippy::cast_possible_truncation)]
                get_edges_pipeline(&frame, camera.get(videoio::CAP_PROP_FRAME_WIDTH)? as i32, camera.get(videoio::CAP_PROP_FRAME_HEIGHT)? as i32)?
            } else {
                frame.clone()
            }.clone();
            highgui::imshow(window, &mat)?;
        }
        let key = highgui::wait_key(10)?;
        if key != -1 {
            if key == 97 { // "a" key
                perform_pipeline = !perform_pipeline;
            } else if key == 113 { // "q" key
                break;
            } else if key == 101 { // "e" key
                println!("{}", marker_location_to_byte(get_marker_location_pipeline(frame,
                                                                                    camera.get(videoio::CAP_PROP_FRAME_WIDTH)? as i32,
                                                                                    camera.get(videoio::CAP_PROP_FRAME_HEIGHT)? as i32)?));
            }
        }
        if highgui::get_window_property(window, 0)? == -1f64 { // This means the window was closed
            highgui::destroy_window(window)?;
            break;
        }
    }
    Ok(())
}