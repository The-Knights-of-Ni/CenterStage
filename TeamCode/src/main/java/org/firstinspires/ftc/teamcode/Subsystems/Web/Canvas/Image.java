package org.firstinspires.ftc.teamcode.Subsystems.Web.Canvas;

import android.graphics.Bitmap;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

public class Image implements CanvasObject {
    Bitmap internal;

    public Image(Bitmap bitmap) {
        internal = bitmap;
    }

    @Override
    public List<Pixel> getPixels() {
        List<Pixel> pixels = new ArrayList<>(1);
        for (int x = 0; x < internal.getWidth(); x++) {
            for (int y = 0; y < internal.getHeight(); y++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    pixels.add(new Pixel(x, y, new RGBA(internal.getColor(x, y))));
                } else {
                    pixels.add(new Pixel(x, y, new RGBA(0, 0, 0, 255)));
                }
            }
        }
        return pixels;
    }

    @Override
    public String toString() {
        return "Image{}";
    }
}
