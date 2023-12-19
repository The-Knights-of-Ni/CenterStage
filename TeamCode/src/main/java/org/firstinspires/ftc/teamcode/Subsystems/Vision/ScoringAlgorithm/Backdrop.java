package org.firstinspires.ftc.teamcode.Subsystems.Vision.ScoringAlgorithm;


import java.util.ArrayList;

public class Backdrop {
    ArrayList<ArrayList<Pixel>> pixels = new ArrayList<>();

    public Backdrop() {
    }

    public Backdrop(ArrayList<ArrayList<Pixel>> pixels) {
        this.pixels = pixels;
    }
}
