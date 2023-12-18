package org.firstinspires.ftc.teamcode.Subsystems.Vision.ScoringAlgorithm;

public class Backdrop {
    //makes sure the number of rows does not cause problems
    public int shortlength = 6;
    public int longlength = 7;
    public int rowamount = 5;
    public Pixel [][] shortRows = new Pixel[rowamount][shortlength];
    public Pixel [][] longRows = new Pixel[rowamount][longlength];
}
