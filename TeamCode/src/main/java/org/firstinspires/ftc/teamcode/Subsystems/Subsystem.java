package org.firstinspires.ftc.teamcode.Subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Superclass to all subsystems, it does some bootstrapping for them (Vision, Control, and Drive)
 */
public abstract class Subsystem {
    // protected instead of private because of inheritance
    protected final Telemetry telemetry;
    protected final String TAG;

    /**
     * inits with telemetry, since every subsystem uses it.
     * @param telemetry The telemetry for logging
     */
    public Subsystem(Telemetry telemetry, String tag) {
        this.telemetry = telemetry;
        this.TAG = tag;
    }
}
