package org.firstinspires.ftc.teamcode.Merlin.Actions;

import org.firstinspires.ftc.teamcode.Subsystems.Web.Canvas;
import org.firstinspires.ftc.teamcode.Util.WebLog;

import java.util.List;

public class SequentialAction implements Action {
    private List<Action> actions;
    private List<Action> initialActions;

    public SequentialAction(List<Action> initialActions) {
        actions = initialActions;
        this.initialActions = initialActions;
    }
    public SequentialAction(Action actions)  {
        this(actions.asList());
    }

    @Override
     public boolean run(WebLog p) {
        if (actions.isEmpty()) {
            return false;
        }

        return if (actions.get(0).run(p)) {
            return true;
        } else {
            actions = actions.drop(1);
            return run(p);
        }
    }

    @Override
    public void preview(Canvas fieldOverlay) {
        for (Action a: initialActions) {
            a.preview(fieldOverlay);
        }
    }
}

