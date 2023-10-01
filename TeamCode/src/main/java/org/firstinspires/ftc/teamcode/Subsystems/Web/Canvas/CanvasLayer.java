package org.firstinspires.ftc.teamcode.Subsystems.Web.Canvas;

import java.util.ArrayList;
import java.util.List;

public class CanvasLayer {
    public List<CanvasObject> objects;
    public int height;
    public int width;

    public CanvasLayer(int height, int width) {
        objects = new ArrayList<>();
        this.height = height;
        this.width = width;
    }

    public RGBA[][] getPixels() {
        RGBA[][] pixelArray = new RGBA[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixelArray[y][x] = new RGBA(0, 0, 0, 0);
            }
        }
        for (CanvasObject canvasObject : objects) {
            for (Pixel p : canvasObject.getPixels()) {
                pixelArray[p.y][p.x] = p.color;
            }
        }
        return pixelArray;
    }
}
