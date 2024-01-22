package org.firstinspires.ftc.teamcode.Auto;

import android.util.Log;
import org.firstinspires.ftc.teamcode.Subsystems.Control.Control;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ArmMovementThread extends Thread {
    public volatile boolean reachedPosition = false;
    public volatile ReentrantLock extended = new ReentrantLock(false);
    Control control;

    public ArmMovementThread(Control control) {
        this.control = control;
    }

    public void run() {
        try {
            extended.tryLock(500, TimeUnit.MILLISECONDS);
            control.moveLinearSlideSync(Control.SCORE_LOW_SLIDE);
            while (!reachedPosition) {
                Thread.sleep(100);
            }
            reachedPosition = false;
            control.openClawSync();
            control.closeClawSync();
            control.moveLinearSlideSync(Control.RETRACTED_SLIDE);

            while (!reachedPosition) {
                Thread.sleep(100);
            }
            control.moveLinearSlideSync(Control.SCORE_LOW_SLIDE);
            control.openClawSync();
            Thread.sleep(200);
            control.closeClawSync();
            control.moveLinearSlideSync(Control.RETRACTED_SLIDE);
            extended.unlock();
        } catch (InterruptedException e) {
            Log.e("ArmMovementThread", "Arm movement thread interrupted", e);
        }
    }
}
