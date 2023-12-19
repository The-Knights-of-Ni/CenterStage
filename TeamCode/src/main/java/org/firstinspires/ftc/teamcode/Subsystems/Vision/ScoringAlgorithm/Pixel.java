package org.firstinspires.ftc.teamcode.Subsystems.Vision.ScoringAlgorithm;
import org.firstinspires.ftc.teamcode.Util.PixelColor;
import org.opencv.core.*;

public class Pixel {
    public PixelColor color;
    //0 for non
    //1 for possible to start
    //2 for already a pixel to start with there
    //3 for can complete mosaic
    public int mosaicPotential = 0;

    public Boolean available = false;

    public int x;

    public int y;
}
