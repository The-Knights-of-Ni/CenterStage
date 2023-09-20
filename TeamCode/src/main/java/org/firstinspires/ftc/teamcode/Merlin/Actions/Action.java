package org.firstinspires.ftc.teamcode.Merlin.Actions;

import org.firstinspires.ftc.teamcode.Subsystems.Web.Canvas;
import org.firstinspires.ftc.teamcode.Util.WebLog;

public interface Action {
    boolean run(WebLog p);

    void preview(Canvas fieldOverlay);
}
