package org.firstinspires.ftc.teamcode.Auto;

import org.firstinspires.ftc.teamcode.Subsystems.Control.Control;

public class ArmMovementThread extends Thread {
    public boolean reachedPosition = false;
    public boolean retracted = false;
    Control control;

    public ArmMovementThread(Control control) {
        this.control = control;
    }

    public void run() {
        control.moveLinearSlideSync(Control.SCORE_LOW_SLIDE);
        while (!reachedPosition) {
        }
        control.openClawSync();
        reachedPosition = false;
        control.moveLinearSlideSync(Control.RETRACTED_SLIDE);
        retracted = true;
        control.closeClawSync();

        while (!reachedPosition) {
        }
        retracted = false;
        control.moveLinearSlideSync(Control.SCORE_LOW_SLIDE);
        control.openClawSync();
        control.moveLinearSlideSync(Control.RETRACTED_SLIDE);
        retracted = true;
    }
}
