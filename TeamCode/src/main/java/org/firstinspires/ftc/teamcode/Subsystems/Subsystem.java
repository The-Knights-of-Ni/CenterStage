package org.firstinspires.ftc.teamcode.Subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Util.MasterLogger;

/**
 * Superclass to all subsystems, it does some bootstrapping for them (Vision, Control, and Drive)
 */
public abstract class Subsystem {
    // protected instead of private because of inheritance
    protected final MasterLogger logger;

    /**
     * inits with telemetry, since every subsystem uses it.
     *
     * @param telemetry The telemetry for logging
     */
    public Subsystem(Telemetry telemetry, String tag) {
        this.logger = new MasterLogger(telemetry, tag);
    }
}
