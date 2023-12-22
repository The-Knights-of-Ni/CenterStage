package org.firstinspires.ftc.teamcode;

import org.junit.jupiter.api.Test;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.ScoringAlgorithm.PixelDetectionPipeline;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class VisionTest {
    @Test
    public void testPixelDetection() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        PixelDetectionPipeline pixelDetectionPipeline = new PixelDetectionPipeline(1920, 720);

        // Reading the Image from the file
        String file = "D:/Documents/Programming/CenterStage/test_resources/test_high_color.jpg";
        Mat image = Imgcodecs.imread(file);
        assert !image.empty();
        Mat output = pixelDetectionPipeline.processFrame(image);
        System.out.println(pixelDetectionPipeline.getPixels());
        String file2 = "D:/Documents/Programming/CenterStage/test_resources/output.webp";
        // Writing the image
        Imgcodecs.imwrite(file2, output);
    }
}
