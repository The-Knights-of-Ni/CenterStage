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
    //pixel in the middle of the bottom row
    public boolean inMosaic_Case3(Pixel thepixel, Pixel left, Pixel topleft, Pixel topright, Pixel right)
    {
        if(!left.partofMosaic && !topleft.partofMosaic && thepixel.color != PixelColor.WHITE && thepixel.color != PixelColor.Empty
        && left.color != PixelColor.WHITE && left.color != PixelColor.Empty && topleft.color != PixelColor.WHITE &&
        topleft.color != PixelColor.Empty && thepixel.color == topleft.color && topleft.color == left.color)
            return true;
        else if(!left.partofMosaic && !topleft.partofMosaic && thepixel.color != PixelColor.WHITE && thepixel.color != PixelColor.Empty
                && left.color != PixelColor.WHITE && left.color != PixelColor.Empty && topleft.color != PixelColor.WHITE &&
                topleft.color != PixelColor.Empty && thepixel.color != topleft.color && topleft.color != left.color && thepixel.color != left.color)
            return true;
        else if(!topright.partofMosaic && !topleft.partofMosaic && thepixel.color != PixelColor.WHITE && thepixel.color != PixelColor.Empty
                && topright.color != PixelColor.WHITE && topright.color != PixelColor.Empty && topleft.color != PixelColor.WHITE &&
                topleft.color != PixelColor.Empty && thepixel.color == topleft.color && topleft.color == topright.color)
            return true;
        else if(!topright.partofMosaic && !topleft.partofMosaic && thepixel.color != PixelColor.WHITE && thepixel.color != PixelColor.Empty
                && topright.color != PixelColor.WHITE && topright.color != PixelColor.Empty && topleft.color != PixelColor.WHITE &&
                topleft.color != PixelColor.Empty && thepixel.color != topleft.color && topleft.color != topright.color && thepixel.color != topright.color)
            return true;
        else if(!topright.partofMosaic && !right.partofMosaic && thepixel.color != PixelColor.WHITE && thepixel.color != PixelColor.Empty
                && topright.color != PixelColor.WHITE && topright.color != PixelColor.Empty && right.color != PixelColor.WHITE &&
                right.color != PixelColor.Empty && thepixel.color == right.color && right.color == topright.color)
            return true;
        else if(!topright.partofMosaic && !right.partofMosaic && thepixel.color != PixelColor.WHITE && thepixel.color != PixelColor.Empty
                && topright.color != PixelColor.WHITE && topright.color != PixelColor.Empty && right.color != PixelColor.WHITE &&
                right.color != PixelColor.Empty && thepixel.color != right.color && right.color != topright.color && thepixel.color != topright.color)
            return true;
        else return false;
    }
    //leftmost and rightmost pixels of short rows (not bottom)
    public boolean inMosaic_Case4(Pixel thepixel, Pixel topside, Pixel topcorner, Pixel sideside, Pixel bottomcorner, Pixel bottomside)
    {
        if(!topside.partofMosaic && !topcorner.partofMosaic && thepixel.color != PixelColor.WHITE && thepixel.color != PixelColor.Empty
                && topside.color != PixelColor.WHITE && topside.color != PixelColor.Empty && topcorner.color != PixelColor.WHITE &&
                topcorner.color != PixelColor.Empty && thepixel.color == topside.color && topside.color == topcorner.color)
            return true;
        if(!topside.partofMosaic && !topcorner.partofMosaic && thepixel.color != PixelColor.WHITE && thepixel.color != PixelColor.Empty
                && topside.color != PixelColor.WHITE && topside.color != PixelColor.Empty && topcorner.color != PixelColor.WHITE &&
                topcorner.color != PixelColor.Empty && thepixel.color != topside.color && topside.color != topcorner.color && thepixel.color != topcorner.color)
            return true;
        if(!sideside.partofMosaic && !topcorner.partofMosaic && thepixel.color != PixelColor.WHITE && thepixel.color != PixelColor.Empty
                && sideside.color != PixelColor.WHITE && sideside.color != PixelColor.Empty && topcorner.color != PixelColor.WHITE &&
                topcorner.color != PixelColor.Empty && thepixel.color == sideside.color && sideside.color == topcorner.color)
            return true;
        if(!sideside.partofMosaic && !topcorner.partofMosaic && thepixel.color != PixelColor.WHITE && thepixel.color != PixelColor.Empty
                && sideside.color != PixelColor.WHITE && sideside.color != PixelColor.Empty && topcorner.color != PixelColor.WHITE &&
                topcorner.color != PixelColor.Empty && thepixel.color != sideside.color && sideside.color != topcorner.color && thepixel.color != topcorner.color)
            return true;
        if(!sideside.partofMosaic && !bottomcorner.partofMosaic && thepixel.color != PixelColor.WHITE && thepixel.color != PixelColor.Empty
                && sideside.color != PixelColor.WHITE && sideside.color != PixelColor.Empty && bottomcorner.color != PixelColor.WHITE &&
                bottomcorner.color != PixelColor.Empty && thepixel.color == sideside.color && sideside.color == bottomcorner.color)
            return true;
        if(!sideside.partofMosaic && !bottomcorner.partofMosaic && thepixel.color != PixelColor.WHITE && thepixel.color != PixelColor.Empty
                && sideside.color != PixelColor.WHITE && sideside.color != PixelColor.Empty && bottomcorner.color != PixelColor.WHITE &&
                bottomcorner.color != PixelColor.Empty && thepixel.color != sideside.color && sideside.color != bottomcorner.color && thepixel.color != bottomcorner.color)
            return true;
        if(!bottomside.partofMosaic && !bottomcorner.partofMosaic && thepixel.color != PixelColor.WHITE && thepixel.color != PixelColor.Empty
                && bottomside.color != PixelColor.WHITE && bottomside.color != PixelColor.Empty && bottomcorner.color != PixelColor.WHITE &&
                bottomcorner.color != PixelColor.Empty && thepixel.color == bottomside.color && bottomside.color == bottomcorner.color)
            return true;
        if(!bottomside.partofMosaic && !bottomcorner.partofMosaic && thepixel.color != PixelColor.WHITE && thepixel.color != PixelColor.Empty
                && bottomside.color != PixelColor.WHITE && bottomside.color != PixelColor.Empty && bottomcorner.color != PixelColor.WHITE &&
                bottomcorner.color != PixelColor.Empty && thepixel.color != bottomside.color && bottomside.color != bottomcorner.color && thepixel.color != bottomcorner.color)
            return true;
        else return false;
    }



}
