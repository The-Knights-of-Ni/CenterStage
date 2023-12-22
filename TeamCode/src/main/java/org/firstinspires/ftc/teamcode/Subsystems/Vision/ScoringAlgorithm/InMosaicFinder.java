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
            return false;
    }

    //left or rightmost pixel of the bottom row
    public boolean inMosaic_Case2(Pixel thepixel, Pixel topside, Pixel corner, Pixel sideside){
        if(!topside.partofMosaic && !corner.partofMosaic && thepixel.color != PixelColor.Empty && thepixel.color != PixelColor.WHITE &&
        topside.color != PixelColor.Empty && topside.color != PixelColor.WHITE && corner.color != PixelColor.Empty &&
        corner.color != PixelColor.WHITE && thepixel.color == topside.color && topside.color == corner.color)
            return true;
        else if(!topside.partofMosaic && !corner.partofMosaic && thepixel.color != PixelColor.Empty && thepixel.color != PixelColor.WHITE &&
                topside.color != PixelColor.Empty && topside.color != PixelColor.WHITE && corner.color != PixelColor.Empty &&
                corner.color != PixelColor.WHITE && thepixel.color != topside.color && topside.color != corner.color && thepixel.color != corner.color)
            return true;
        else if(!sideside.partofMosaic && !corner.partofMosaic && thepixel.color != PixelColor.Empty && thepixel.color != PixelColor.WHITE &&
                sideside.color != PixelColor.Empty && sideside.color != PixelColor.WHITE && corner.color != PixelColor.Empty &&
                corner.color != PixelColor.WHITE && thepixel.color == sideside.color && sideside.color == corner.color)
            return true;
        else if(!sideside.partofMosaic && !corner.partofMosaic && thepixel.color != PixelColor.Empty && thepixel.color != PixelColor.WHITE &&
                sideside.color != PixelColor.Empty && sideside.color != PixelColor.WHITE && corner.color != PixelColor.Empty &&
                corner.color != PixelColor.WHITE && thepixel.color != sideside.color && sideside.color != corner.color && thepixel.color != corner.color)
            return true;
        else return false;
    }


}
