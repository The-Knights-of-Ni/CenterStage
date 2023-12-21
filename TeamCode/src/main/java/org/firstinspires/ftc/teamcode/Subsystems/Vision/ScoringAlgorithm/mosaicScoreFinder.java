package org.firstinspires.ftc.teamcode.Subsystems.Vision.ScoringAlgorithm;

import org.firstinspires.ftc.teamcode.Util.PixelColor;

public class mosaicScoreFinder {
    //Case1 for leftmost or rightmost pixel of long row
    public int scoreFinder_Case1(Pixel givenpixel, Pixel bottom, Pixel side)
    {
        if(side.partofMosaic)
        {
            if (givenpixel.color != PixelColor.WHITE)
                return 1;
            else
                return 0;
        }
        else if(bottom.partofMosaic && !side.partofMosaic)
        {
            if (givenpixel.color != PixelColor.WHITE)
                return 2;
            else
                return -2;
        }
        //both pixels are not the same color, no mosaics involved
        else if(bottom.color != side.color && bottom.color != PixelColor.WHITE && side.color != PixelColor.WHITE)
        {
            if(givenpixel.color != PixelColor.WHITE && givenpixel.color != bottom.color && givenpixel.color != side.color)
                return 3;

            else
                return -3;
        }
        //both pixels are the same color, no mosaics involved
        else if(bottom.color == side.color && bottom.color != PixelColor.WHITE)
        {
            if(givenpixel.color == bottom.color)
                return 3;
            else
                return -3;
        }
        //if right is white or both are white, no mosiacs involved
        else if(side.color == PixelColor.WHITE) {
            if (givenpixel.color != PixelColor.WHITE)
                return 1;
            else
                return 0;
        }
        //if bottom is white and right is something else, no mosaics invovled
        else if(bottom.color == PixelColor.WHITE && side.color != PixelColor.WHITE) {
            if (givenpixel.color != PixelColor.WHITE)
                return 2;
            else
                return -2;
        }
        //failsafe
        else
            return 0;
    }

    //for the left most and right most pixel of the bottom row
    public int scoreFinder_Case2 (Pixel givenpixel, Pixel side)
    {
        //the side is part of a mosaic
        if (side.partofMosaic)
        {
            if (givenpixel.color != PixelColor.WHITE)
                return 1;
            else
                return 0;
        }
        //side pixel is empty or white
        else if(side.color == PixelColor.Empty || side.color == PixelColor.WHITE) {
            if (givenpixel.color != PixelColor.WHITE)
                return 1;
            else
                return 0;
        }
        //side pixel has color
        else if(side.color != PixelColor.WHITE)
        {
            if(givenpixel.color != PixelColor.WHITE)
                return 2;
            else
                return -2;
        }
        //failsafe
        else
            return 0;
    }

    //for a pixel in the middle of the bottom row
    public int scoreFinder_Case3 (Pixel givenpixel, Pixel left, Pixel right)
    {
        //both sides are part of a mosiac
        if(left.partofMosaic&& right.partofMosaic)
        {
            if (givenpixel.color != PixelColor.WHITE)
                return 1;
            else
                return 0;
        }
        //one is part of mosaic
        else if((left.partofMosaic && !right.partofMosaic) || (right.partofMosaic && !left.partofMosaic))
        {
            if((left.color != PixelColor.WHITE) && (left.color != PixelColor.Empty)
                    && (right.color != PixelColor.WHITE) && (right.color != PixelColor.Empty))
            {
                if (givenpixel.color != PixelColor.WHITE)
                    return 2;
                else
                    return -2;
            }
            else{
                if (givenpixel.color != PixelColor.WHITE)
                    return 1;
                else
                    return 0;
            }
        }
        //non are part of a mosiac, both or one side are colored
        else if(((left.color != PixelColor.WHITE)&& (left.color != PixelColor.Empty))
                || ((right.color != PixelColor.WHITE) && (right.color != PixelColor.Empty)))
        {
            if (givenpixel.color != PixelColor.WHITE)
                return 2;
            else
                return -2;
        }
        //non are colored
        else if(((left.color == PixelColor.WHITE) || (left.color != PixelColor.Empty))
                && ((right.color != PixelColor.WHITE) || (right.color != PixelColor.Empty))) {
            if (givenpixel.color != PixelColor.WHITE)
                return 1;
            else
                return 0;
        }
        //failsafe
        else
            return 0;
    }

    //leftmost or rightmost pixels of short Rows
    public int scoreFinder_Case4(Pixel givenpixel, Pixel side, Pixel bottomright, Pixel bottomleft)
    {
        //if all are part of a mosiac
        if(side.partofMosaic&&bottomright.partofMosaic&& bottomright.partofMosaic)
        {
            if(givenpixel.color != PixelColor.WHITE)
                return 1;
            else
                return 0;
        }

        //failsafe
        else
            return 0;
    }
}
