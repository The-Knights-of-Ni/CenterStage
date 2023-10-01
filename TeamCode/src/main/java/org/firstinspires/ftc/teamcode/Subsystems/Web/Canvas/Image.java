package org.firstinspires.ftc.teamcode.Subsystems.Web.Canvas;

import android.graphics.Bitmap;

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
                pixels.add(new Pixel(x, y, new RGBA(internal.getColor(x, y))));
            }
        }
        return null;
    }
}
