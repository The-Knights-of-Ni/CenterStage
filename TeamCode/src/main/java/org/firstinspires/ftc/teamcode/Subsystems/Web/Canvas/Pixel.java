package org.firstinspires.ftc.teamcode.Subsystems.Web.Canvas;

import org.firstinspires.ftc.teamcode.Util.Vector;

public class Pixel {
    public int x;
    public int y;
    public RGBA color;

    public Pixel(int x, int y, RGBA color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public Pixel(Vector coord, RGBA color) {
        this((int) coord.getX(), (int) coord.getY(), color);
    }
}
