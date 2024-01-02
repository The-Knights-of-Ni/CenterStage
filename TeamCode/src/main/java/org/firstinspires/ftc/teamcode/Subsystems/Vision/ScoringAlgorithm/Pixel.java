package org.firstinspires.ftc.teamcode.Subsystems.Vision.ScoringAlgorithm;

import org.firstinspires.ftc.teamcode.Util.PixelColor;
import org.opencv.core.*;

public class Pixel {
    public PixelColor color;
    //0 for non
    //1 for possible to start
    //2 for already a pixel to start with there
    //3 for can complete mosaic
    //negates for things that can mess up mosaic sets
    public int mosaicPotential = 0;

    //1 or 0
    public int heightPotential = 0;

    public Boolean partofMosaic = false;

    public Boolean available = false;

    public int x;

    public int y;
}
