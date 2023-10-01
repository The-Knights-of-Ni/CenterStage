package org.firstinspires.ftc.teamcode.Subsystems.Web.Canvas;

import java.util.ArrayList;
import java.util.List;

public class CanvasPixel  implements CanvasObject{
    Pixel pixel;

    public CanvasPixel(Pixel pixel) {
        this.pixel = pixel;
    }


    @Override
    public List<Pixel> getPixels() {
        List<Pixel> list = new ArrayList<>(1);
        list.add(pixel);
        return list;
    }
}
