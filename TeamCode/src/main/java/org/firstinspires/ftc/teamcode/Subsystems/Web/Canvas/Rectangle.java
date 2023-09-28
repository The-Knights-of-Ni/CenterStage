package org.firstinspires.ftc.teamcode.Subsystems.Web.Canvas;

import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Rectangle implements CanvasObject {
    public int height;
    public int width;
    public RGBA color;
    public Vector topLeft;

    public Rectangle(int height, int width, Vector topLeft, RGBA color) {
        this.height = height;
        this.width = width;
        this.topLeft = topLeft;
        this.color = color;
    }


    @Override
    public List<Pixel> getPixels() {
        List<Pixel> pixels = new ArrayList<>(1);
        for (int x = (int) topLeft.getX(); x <= width; x++) {
            for (int y = (int) topLeft.getY(); y <= height; y++) {
                pixels.add(new Pixel(x, y, color));
            }
        }
        return pixels;
    }
}
