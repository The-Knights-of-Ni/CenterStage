package org.firstinspires.ftc.teamcode.Subsystems.Control;

public class ScorePixelThread extends Thread {
    private final Control control;

    public ScorePixelThread(Control control) {
        this.control = control;
    }

    @Override
    public void run() {
        control.closeClaw();
        control.moveLinearSlideSync(Control.SCORE_LOW_SLIDE);
        control.openClawSync();
        control.closeClawSync();
        control.moveLinearSlideSync(Control.RETRACTED_SLIDE);
    }
}
