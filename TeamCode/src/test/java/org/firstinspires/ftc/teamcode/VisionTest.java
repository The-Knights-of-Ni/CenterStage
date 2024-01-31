package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.Subsystems.Vision.MarkerDetectionPipeline;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.junit.jupiter.api.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class VisionTest {
    @Test
    public void testPixelDetection() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        MarkerDetectionPipeline pixelDetectionPipeline = new MarkerDetectionPipeline(AllianceColor.RED);

        // Reading the Image from the file
        String file = "D:/Documents/Programming/CenterStage/test_resources/red_test.jpg";
        Mat image = Imgcodecs.imread(file);
        assert !image.empty();
        Mat output = pixelDetectionPipeline.processFrame(image);
        System.out.println(pixelDetectionPipeline.getMarkerLocation());
        String file2 = "D:/Documents/Programming/CenterStage/test_resources/output.webp";
        // Writing the image
        Imgcodecs.imwrite(file2, output);
    }
}
