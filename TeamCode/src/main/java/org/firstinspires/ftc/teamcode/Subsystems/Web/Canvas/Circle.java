package org.firstinspires.ftc.teamcode.Subsystems.Web.Canvas;

import org.firstinspires.ftc.teamcode.Util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Circle implements CanvasObject {
    public int radius;
    public RGBA color;
    public Vector center;

    public Circle(int radius, Vector center, RGBA color) {
        this.radius = radius;
        this.center = center;
        this.color = color;
    }


    @Override
    public List<Pixel> getPixels() {
        List<Pixel> pixels = new ArrayList<>(1);
        pixels.add(new Pixel(center, color));
        return pixels;
    }
}
