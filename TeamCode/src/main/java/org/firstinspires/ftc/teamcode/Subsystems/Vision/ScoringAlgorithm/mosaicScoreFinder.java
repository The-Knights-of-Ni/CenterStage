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
        //if side is white or both are white, no mosaics involved
        else if(side.color == PixelColor.WHITE) {
            if (givenpixel.color != PixelColor.WHITE)
                return 1;
            else
                return 0;
        }
        //if bottom is white and side is something else, no mosaics involved
        else if(bottom.color == PixelColor.WHITE && side.color != PixelColor.WHITE) {
            if (givenpixel.color != PixelColor.WHITE)
                return 2;
            else
                return -2;
        }
        //failsafe
        else
            return -1000;
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
            return -1000;
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
        else if(((left.color == PixelColor.WHITE) || (left.color == PixelColor.Empty))
                && ((right.color == PixelColor.WHITE) || (right.color == PixelColor.Empty))) {
            if (givenpixel.color != PixelColor.WHITE)
                return 1;
            else
                return 0;
        }
        //failsafe
        else
            return -1000;
    }

    //leftmost or rightmost pixels of short Rows (not bottom)
    public int scoreFinder_Case4(Pixel givenpixel, Pixel side, Pixel bottomright, Pixel bottomleft)
    {
        //if all are part of a mosaic
        if(side.partofMosaic&&bottomright.partofMosaic&& bottomleft.partofMosaic)
        {
            if(givenpixel.color != PixelColor.WHITE)
                return 1;
            else
                return 0;
        }
        //if side and bottom right are parts of mosaics
        else if(side.partofMosaic && bottomright.partofMosaic && !bottomleft.partofMosaic)
        {
            if(givenpixel.color != PixelColor.WHITE)
                return 1;
            else
                return 0;
        }
        //if side and bottom left are parts of mosaics
        else if(side.partofMosaic && !bottomright.partofMosaic && bottomleft.partofMosaic)
        {
            if(givenpixel.color != PixelColor.WHITE)
                return 1;
            else
                return 0;
        }
        //if bottom right and bottom left are parts of mosaics
        else if(!side.partofMosaic && bottomright.partofMosaic && bottomleft.partofMosaic)
        {
            if((side.color != PixelColor.WHITE)&&(side.color != PixelColor.Empty))
            {
                if(givenpixel.color != PixelColor.WHITE)
                    return 2;
                else
                    return -2;
            }
            else
            {
                if(givenpixel.color != PixelColor.WHITE)
                    return 1;
                else
                    return 0;
            }
        }
        //if bottom left is the only one part of a mosaic
        else if(!side.partofMosaic && !bottomright.partofMosaic && bottomleft.partofMosaic)
        {
            //if side and bottom right are both colored
            if(((side.color != PixelColor.WHITE) && (side.color != PixelColor.Empty)) &&
                    (bottomright.color != PixelColor.WHITE))
            {
                //the two pixel's colors are not equal
                if(side.color != bottomright.color)
                {
                    if(givenpixel.color != PixelColor.WHITE && givenpixel.color != side.color
                    && givenpixel.color != bottomright.color)
                        return 3;
                    else
                        return -3;
                }
                //equal
                else
                {
                    if(givenpixel.color != PixelColor.WHITE && givenpixel.color != side.color)
                        return 3;
                    else
                        return -3;
                }
            }
            //side is colored but bottom right is not
            if(((side.color != PixelColor.WHITE) && (side.color != PixelColor.Empty)) &&
                    !(bottomright.color != PixelColor.WHITE))
            {
                if(givenpixel.color != PixelColor.WHITE)
                    return 2;
                else
                    return -2;
            }
            //side is not colored
            else
            {
                if (givenpixel.color != PixelColor.WHITE)
                    return 1;
                else
                    return 0;
            }
        }
        //bottom right is the only one part of a mosaic
        else if(!side.partofMosaic && bottomright.partofMosaic && !bottomleft.partofMosaic) {
            if (side.color != PixelColor.Empty && side.color != PixelColor.WHITE)
            {
                if(givenpixel.color != PixelColor.WHITE)
                    return 2;
                else
                    return -2;
            }
            else
            {
                if(givenpixel.color != PixelColor.WHITE)
                    return 1;
                else
                    return 0;
            }
        }
        //side is the only one part of a mosaic
        else if(side.partofMosaic && !bottomright.partofMosaic && !bottomleft.partofMosaic)
        {
            if(bottomright.color != PixelColor.WHITE && bottomleft.color != PixelColor.WHITE)
            {
                if(bottomright.color != bottomleft.color)
                {
                    if(givenpixel.color != bottomright.color && givenpixel.color != bottomleft.color
                    && givenpixel.color != PixelColor.WHITE)
                        return 3;
                    else
                        return -3;
                }
                else
                {
                    if(givenpixel.color != PixelColor.WHITE && givenpixel.color == bottomright.color)
                        return 3;
                    else
                        return -3;
                }
            }
            else if(bottomright.color == PixelColor.WHITE && bottomleft.color == PixelColor.WHITE)
            {
                if(givenpixel.color != PixelColor.WHITE)
                    return 1;
                else
                    return 0;
            }
            else{
                if(givenpixel.color != PixelColor.WHITE)
                    return 1;
                else
                    return 0;
            }
        }
        //no mosaics involved, all are white or empty
        else if(((side.color == PixelColor.Empty)||(side.color == PixelColor.WHITE))
                &&bottomleft.color == PixelColor.WHITE&&bottomright.color == PixelColor.WHITE)
        {
            if(givenpixel.color != PixelColor.WHITE)
                return 1;
            else
                return 0;
        }
        //if side and bottom right are white or empty
        else if((side.color ==PixelColor.Empty)||(side.color == PixelColor.WHITE)
                && bottomright.color == PixelColor.WHITE && bottomleft.color != PixelColor.WHITE)
        {
            if(givenpixel.color != PixelColor.WHITE)
                return 1;
            else
                return 0;
        }

        //if side and bottom left are white or empty
        else if(side.color == PixelColor.Empty || side.color == PixelColor.WHITE
        &bottomright.color!= PixelColor.WHITE && bottomleft.color == PixelColor.WHITE )
        {
            if(givenpixel.color != PixelColor.WHITE)
                return 1;
            else
                return 0;
        }

        //if bottom right and bottom left are white
        else if(side.color != PixelColor.WHITE && side.color != PixelColor.Empty
                && bottomright.color == PixelColor.WHITE && bottomleft.color == PixelColor.WHITE)
        {
            if(givenpixel.color != PixelColor.WHITE)
                return 2;
            else
                return -2;
        }
        //if bottom left is the only one white
        else if(side.color != PixelColor.WHITE && side.color != PixelColor.Empty
        && bottomright.color != PixelColor.WHITE && bottomleft.color == PixelColor.WHITE)
        {

                //the two pixel's colors are not equal
            if(side.color != bottomright.color)
                {
                    if(givenpixel.color != PixelColor.WHITE && givenpixel.color != side.color
                            && givenpixel.color != bottomright.color)
                        return 3;
                    else
                        return -3;
                }
                //equal
            else
                {
                    if(givenpixel.color != PixelColor.WHITE && givenpixel.color != side.color)
                        return 3;
                    else
                        return -3;
                }
        }
        //if bottom right is the only one white
        else if(side.color != PixelColor.WHITE && side.color != PixelColor.Empty
        && bottomright.color == PixelColor.WHITE && bottomleft.color != PixelColor.WHITE)
        {
            if(givenpixel.color != PixelColor.WHITE)
                return 2;
            else
                return 2;
        }

        //side is white or empty
        else if((side.color == PixelColor.Empty || side.color == PixelColor.WHITE)
                && bottomleft.color != PixelColor.WHITE && bottomright.color != PixelColor.WHITE)
        {

            if(bottomright.color != bottomleft.color)
            {
                if(givenpixel.color != bottomright.color && givenpixel.color != bottomleft.color
                        && givenpixel.color != PixelColor.WHITE)
                    return 3;
                else
                    return -3;
            }
            else
            {
                if(givenpixel.color != PixelColor.WHITE && givenpixel.color == bottomright.color)
                    return 3;
                else
                    return -3;
            }
        }
        else if(side.color != PixelColor.Empty && side.color != PixelColor.WHITE
        && bottomleft.color != PixelColor.WHITE && bottomright.color != PixelColor.WHITE)
        {
            if(bottomright.color != bottomleft.color)
            {
                if(givenpixel.color != bottomright.color && givenpixel.color != bottomleft.color
                        && givenpixel.color != PixelColor.WHITE)
                    return 3;
                else
                {
                    //the two pixel's colors are not equal
                    if(side.color != bottomright.color)
                    {
                        if(givenpixel.color != PixelColor.WHITE && givenpixel.color != side.color
                                && givenpixel.color != bottomright.color)
                            return 3;
                        else
                            return -3;
                    }
                    //equal
                    else
                    {
                        if(givenpixel.color != PixelColor.WHITE && givenpixel.color != side.color)
                            return 3;
                        else
                            return -3;
                    }
                }
            }
            else
            {
                if(givenpixel.color != PixelColor.WHITE && givenpixel.color == bottomright.color)
                    return 3;
                else
                {
                    //the two pixel's colors are not equal
                    if(side.color != bottomright.color)
                    {
                        if(givenpixel.color != PixelColor.WHITE && givenpixel.color != side.color
                                && givenpixel.color != bottomright.color)
                            return 3;
                        else
                            return -3;
                    }
                    //equal
                    else
                    {
                        if(givenpixel.color != PixelColor.WHITE && givenpixel.color != side.color)
                            return 3;
                        else
                            return -3;
                    }
                }
            }
        }
        //failsafe
        else
            return -1000;
    }
    //if pixel is surrounded by all
    public int scoreFinder_Case5(Pixel givenpixel, Pixel topleft, Pixel top, Pixel topright,
                                 Pixel bottomright, Pixel bottom, Pixel bottomleft) {
        if(false)
        {
            return 0;
        }
        //failsafe
        else
            return -1000;
    }
}
