package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

public class GamepadManager {
    public GamepadWrapper gamepad1;
    public GamepadWrapper gamepad2;
    public GamepadManager(Gamepad gamepad1, Gamepad gamepad2) {
        this.gamepad1 = new GamepadWrapper(gamepad1);
        this.gamepad2 = new GamepadWrapper(gamepad2);
        update();
    }

    public void update() {
        gamepad1.update();
        gamepad2.update();
    }
}