package org.firstinspires.ftc.teamcode.Subsystems.Web.Canvas;

import android.graphics.Color;

public class RGBA {
    public short r;
    public short g;
    public short b;
    public short a;

    public RGBA(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public RGBA(int r, int g, int b, int a) {
        assert r < 256;
        assert g < 256;
        assert b < 256;
        assert a < 256;
        this.r = (short) r;
        this.g = (short) g;
        this.b = (short) b;
        this.a = (short) a;
    }

    public RGBA(Color color) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int colorInt = color.toArgb();
            this.a = (short) ((colorInt >> 24) & 0xff); // or color >>> 24
            this.r = (short) ((colorInt >> 16) & 0xff);
            this.g = (short) ((colorInt >> 8) & 0xff);
            this.b = (short) ((colorInt) & 0xff);
        } else {
            this.a = 255;
            this.r = 0;
            this.g = 0;
            this.b = 0;
        }
    }

    public static RGBA overlay(RGBA newPixel, RGBA oldPixel) {
        assert oldPixel.a == 255;
        short alphaMultiplier = (short) (255 - newPixel.a);
        return new RGBA(
                alphaMultiplier * oldPixel.r + (short) (newPixel.r * (((double) newPixel.a) / 255)),
                alphaMultiplier * oldPixel.g + (short) (newPixel.g * (((double) newPixel.a) / 255)),
                alphaMultiplier * oldPixel.b + (short) (newPixel.b * (((double) newPixel.a) / 255)),
                255);
    }

    public int toInt() {
        return (a & 0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 8 | (b & 0xff);
    }
}
