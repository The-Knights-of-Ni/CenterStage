package org.firstinspires.ftc.teamcode.Subsystems.Vision.ScoringAlgorithm;

import org.firstinspires.ftc.teamcode.Util.PixelColor;

public class InMosaicFinder {
    //leftmost and rightmost pixel of long row
    public boolean inMosaic_Case1(Pixel thepixel, Pixel top, Pixel side, Pixel bottom) {
        if (!top.partofMosaic && !side.partofMosaic && thepixel.color != PixelColor.Empty && thepixel.color != PixelColor.WHITE
                && top.color != PixelColor.Empty && top.color != PixelColor.WHITE && side.color != PixelColor.Empty
                && side.color != PixelColor.WHITE && top.color == side.color && top.color == thepixel.color)
            return true;
        else if (!top.partofMosaic && !side.partofMosaic && thepixel.color != PixelColor.Empty && thepixel.color != PixelColor.WHITE
                && top.color != PixelColor.Empty && top.color != PixelColor.WHITE && side.color != PixelColor.Empty
                && side.color != PixelColor.WHITE && top.color != side.color && top.color != thepixel.color && thepixel.color != side.color)
            return true;
        else if (!bottom.partofMosaic && !side.partofMosaic && thepixel.color != PixelColor.Empty && thepixel.color != PixelColor.WHITE
                && bottom.color != PixelColor.Empty && bottom.color != PixelColor.WHITE && side.color != PixelColor.Empty
                && side.color != PixelColor.WHITE && bottom.color == side.color && bottom.color == thepixel.color)
            return true;
        else if (!bottom.partofMosaic && !side.partofMosaic && thepixel.color != PixelColor.Empty && thepixel.color != PixelColor.WHITE
                && bottom.color != PixelColor.Empty && bottom.color != PixelColor.WHITE && side.color != PixelColor.Empty
                && side.color != PixelColor.WHITE && bottom.color != side.color && bottom.color != thepixel.color && thepixel.color != side.color)
            return true;
        else
            return true;

    }


}
