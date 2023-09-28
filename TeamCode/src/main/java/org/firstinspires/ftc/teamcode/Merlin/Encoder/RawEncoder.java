package org.firstinspires.ftc.teamcode.Merlin.Encoder;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class RawEncoder {
    DcMotorEx m;
    DcMotorSimple.Direction direction = DcMotorSimple.Direction.FORWARD;
    DcMotorController controller;

    public RawEncoder(DcMotorEx m) {
        this.m = m;
        this.controller = m.getController();
    }

    private int applyDirection(int x) {
        if (m.getDirection() == DcMotorSimple.Direction.REVERSE) {
            x = -x;
        }

        if (direction == DcMotorSimple.Direction.REVERSE) {
            x = -x;
        }

        return x;
    }

    public PositionVelocityPair getPositionAndVelocity() {
        return new PositionVelocityPair(
                applyDirection(m.getCurrentPosition()),
                applyDirection((int) m.getVelocity())
        );
    }
}
