package org.firstinspires.ftc.teamcode.Subsystems.Vision.ScoringAlgorithm;

import org.firstinspires.ftc.teamcode.Util.PixelColor;

import java.util.Arrays;

public class Backdrop {
    // Makes sure the number of rows does not cause problems
    public final int shortLength = 6;
    public final int longLength = 7;
    public final int rowAmount = 5;
    public Pixel[][] rows = new Pixel[rowAmount + rowAmount][shortLength + longLength];

    public Backdrop() {
    }

    public boolean isValid() {
        for (int i = 0; i < rowAmount; i += 2) {
            if (rows[i].length != shortLength) {
                return false;
            }
        }
        for (int i = 1; i < rowAmount; i += 2) {
            if (rows[i].length != longLength) {
                return false;
            }
        }
        return true;
    }

    public void updateMosaicPossibilities() {
        for (int i = 0; i < rowAmount; i++) {
            for (int j = 0; j < rows[i].length; j++) {
                Pixel pixel = rows[i][j];
                if (pixel.color != PixelColor.WHITE) {
                    pixel.mosaicPotential = Pixel.MosaicPotential.NONE;
                } else {

                }
            }
        }
    }

    @Override
    public String toString() { // TODO: print out pixels in better format
        return Arrays.toString(rows);
    }
}
