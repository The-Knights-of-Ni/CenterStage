package org.firstinspires.ftc.teamcode.Subsystems.Web.Canvas;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class WebCanvas {
    public List<CanvasLayer> layers;
    public final int height;
    public final int width;

    public WebCanvas(int height, int width) {
        this.width = width;
        this.height = height;
        this.layers = new ArrayList<>(1);
    }

    public RGBA[][] getPixels() {
        RGBA[][] pixelArray = new RGBA[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixelArray[y][x] = new RGBA(255, 255, 255, 255); // TODO: set background color variable
            }
        }
        for (CanvasLayer canvasLayer: layers) {
            RGBA[][] layerPixels = canvasLayer.getPixels();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    RGBA newPixel = layerPixels[y][x];
                    RGBA oldPixel = pixelArray[y][x];
                    if (newPixel.a != 0) {
                        pixelArray[y][x] = RGBA.overlay(newPixel, oldPixel);
                    }
                }
            }
        }
        return pixelArray;
    }

    public Bitmap toBitmap() {
        RGBA[][] pixels = getPixels();
        int[] intPixels = new int[pixels.length * pixels[0].length];
        int counter = 0;
        for (RGBA[] row : pixels) {
            for (RGBA pixel : row) {
                intPixels[counter] = pixel.toInt();
                counter++;
            }
        }
        return Bitmap.createBitmap(intPixels, pixels[0].length, pixels.length, Bitmap.Config.ARGB_8888);
    }
}
