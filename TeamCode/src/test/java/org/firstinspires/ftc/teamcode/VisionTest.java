package org.firstinspires.ftc.teamcode;

import android.util.Log;
import org.firstinspires.ftc.teamcode.Subsystems.Vision.MarkerDetectionPipeline;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import static org.mockito.Mockito.mockStatic;

public class VisionTest {
    @Test
    public void testMarkerDetection() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        MarkerDetectionPipeline pixelDetectionPipeline = new MarkerDetectionPipeline(AllianceColor.BLUE);

        // Reading the Image from the file
        String file = "D:/Documents/Programming/CenterStage/test_resources/blue_test.jpg";
        Mat image = Imgcodecs.imread(file);
        assert !image.empty();
        try (MockedStatic<Log> mocked = mockStatic(Log.class)) {
            Mat output = pixelDetectionPipeline.processFrame(image);
            System.out.println(pixelDetectionPipeline.getMarkerLocation());
            String file2 = "D:/Documents/Programming/CenterStage/test_resources/output.jpg";
            // Writing the image
            Imgcodecs.imwrite(file2, output);
        }
    }
}
