package org.firstinspires.ftc.teamcode.Subsystems.Vision.ScoringAlgorithm;

import org.firstinspires.ftc.teamcode.Util.PixelColor;
import org.opencv.core.*;

public class Pixel {
    public PixelColor color;

    public enum MosaicPotential {
        NONE,
        POSSIBLE_TO_START,
        ALREADY_A_PIXEL_TO_START,
        CAN_COMPLETE_MOSAIC,
        PLACED
    }

    public MosaicPotential mosaicPotential = MosaicPotential.NONE;
}
