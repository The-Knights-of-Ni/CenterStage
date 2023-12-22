package org.firstinspires.ftc.teamcode.Subsystems.Vision.ScoringAlgorithm;

import org.firstinspires.ftc.teamcode.Util.PixelColor;

import java.util.Arrays;

public class Backdrop {
    public class Coordinates {
        public int row;
        public int col;

        public Coordinates(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

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

    public Coordinates[] getNeighbors(Coordinates coordinates) {
        int row = coordinates.row;
        int col = coordinates.col;
        if (row % 2 == 0) {
            if (col == 0) {
                return new Coordinates[] {
                        new Coordinates(row - 1, col),
                        new Coordinates(row - 1, col + 1),
                        new Coordinates(row, col + 1),
                        new Coordinates(row + 1, col),
                        new Coordinates(row, col + 1),
                        new Coordinates(row + 1, col)
                };
            } else if (col == shortLength - 1) {
                return new Coordinates[] {
                        new Coordinates(row - 1, col),
                        new Coordinates(row - 1, col + 1),
                        new Coordinates(row, col - 1),
                        new Coordinates(row + 1, col),
                        new Coordinates(row, col - 1),
                        new Coordinates(row + 1, col)
                };
            } else {
                return new Coordinates[] {
                        new Coordinates(row - 1, col),
                        new Coordinates(row - 1, col + 1),
                        new Coordinates(row, col - 1),
                        new Coordinates(row, col + 1),
                        new Coordinates(row + 1, col),
                        new Coordinates(row + 1, col + 1)
                };
            }
        } else {
            if (col == 0) {
                return new Coordinates[]{
                        new Coordinates(row - 1, col),
                        new Coordinates(row - 1, col + 1),
                        new Coordinates(row, col + 1),
                        new Coordinates(row + 1, col),
                        new Coordinates(row, col + 1),
                        new Coordinates(row + 1, col)
                };
            } else if (col == longLength - 1) {
                return new Coordinates[]{
                        new Coordinates(row - 1, col),
                        new Coordinates(row - 1, col + 1),
                        new Coordinates(row, col - 1),
                        new Coordinates(row + 1, col),
                        new Coordinates(row, col - 1),
                        new Coordinates(row + 1, col)
                };
            } else {
                return new Coordinates[]{
                        new Coordinates(row - 1, col),
                        new Coordinates(row - 1, col + 1),
                        new Coordinates(row, col - 1),
                        new Coordinates(row, col + 1),
                        new Coordinates(row + 1, col),
                        new Coordinates(row + 1, col + 1)
                };
            }
        }
    }

    public Coordinates[] getNeighbors(int row, int col) {
        return getNeighbors(new Coordinates(row, col));
    }

    public void updateMosaicPossibilities() {
        for (int i = 0; i < rowAmount; i++) {
            for (int j = 0; j < rows[i].length; j++) {
                Pixel pixel = rows[i][j];
                if (pixel.color == PixelColor.WHITE) {
                    pixel.mosaicPotential = Pixel.MosaicPotential.NONE;
                } else if (pixel.color != PixelColor.EMPTY) {
                    pixel.mosaicPotential = Pixel.MosaicPotential.PLACED;
                } else {
                    var neighbors = getNeighbors(i, j);
                }
            }
        }
    }

    @Override
    public String toString() { // TODO: print out pixels in better format
        return Arrays.toString(rows);
    }
}
