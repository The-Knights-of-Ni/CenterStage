package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

public class GamepadManager {
    private static final double joystickDeadZone = 0.1;

    public double leftStickX;
    public double leftStickY;
    public double rightStickX;
    public double rightStickY;
    public float triggerLeft;
    public float triggerRight;
    public Button aButton = new Button();
    public Button bButton = new Button();
    public Button xButton = new Button();
    public Button yButton = new Button();
    public Button dPadUp = new Button();
    public Button dPadDown = new Button();
    public Button dPadLeft = new Button();
    public Button dPadRight = new Button();
    public Button bumperLeft = new Button();
    public Button bumperRight = new Button();
    public double leftStickX2;
    public double leftStickY2;
    public double rightStickX2;
    public double rightStickY2;
    public float triggerLeft2;
    public float triggerRight2;
    public Button aButton2 = new Button();
    public Button bButton2 = new Button();
    public Button xButton2 = new Button();
    public Button yButton2 = new Button();
    public Button dPadUp2 = new Button();
    public Button dPadDown2 = new Button();
    public Button dPadLeft2 = new Button();
    public Button dPadRight2 = new Button();
    public Button bumperLeft2 = new Button();
    public Button bumperRight2 = new Button();
    public Gamepad gamepad1;
    public Gamepad gamepad2;
    public GamepadManager(Gamepad gamepad1, Gamepad gamepad2) {
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        update();
    }

    /**
     * Discards joystick inputs between -joystickDeadZone and joystickDeadZone
     * @param joystickInput the input of the joystick
     * @return the corrected input of the joystick
     */
    private double joystickDeadzoneCorrection(double joystickInput) {
        double joystickOutput;
        if (joystickInput > joystickDeadZone) {
            joystickOutput = (joystickInput - joystickDeadZone) / (1.0 - joystickDeadZone);
        } else if (joystickInput > -joystickDeadZone) { // joystickInput is between -joystickDeadZone and joystickDeadZone
            joystickOutput = 0.0;
        } else { // if joystickInput is less than -joystickDeadZone
            joystickOutput = (joystickInput + joystickDeadZone) / (1.0 - joystickDeadZone);
        }
        return joystickOutput;
    }

    public void update() {
        leftStickX = joystickDeadzoneCorrection(gamepad1.left_stick_x);
        leftStickY = joystickDeadzoneCorrection(-gamepad1.left_stick_y);
        rightStickX = joystickDeadzoneCorrection(gamepad1.right_stick_x);
        rightStickY = joystickDeadzoneCorrection(gamepad1.right_stick_y);
        triggerLeft = gamepad1.left_trigger;
        triggerRight = gamepad1.right_trigger;
        aButton.update(gamepad1.a);
        bButton.update(gamepad1.b);
        xButton.update(gamepad1.x);
        yButton.update(gamepad1.y);
        dPadUp.update(gamepad1.dpad_up);
        dPadDown.update(gamepad1.dpad_down);
        dPadLeft.update(gamepad1.dpad_left);
        dPadRight.update(gamepad1.dpad_right);
        bumperLeft.update(gamepad1.left_bumper);
        bumperRight.update(gamepad1.right_bumper);
        leftStickX2 = joystickDeadzoneCorrection(gamepad2.left_stick_x);
        leftStickY2 = joystickDeadzoneCorrection(-gamepad2.left_stick_y);
        rightStickX2 = joystickDeadzoneCorrection(gamepad2.right_stick_x);
        rightStickY2 = joystickDeadzoneCorrection(-gamepad2.right_stick_y);
        triggerLeft2 = gamepad2.left_trigger;
        triggerRight2 = gamepad2.right_trigger;
        aButton2.update(gamepad2.a);
        bButton2.update(gamepad2.b);
        xButton2.update(gamepad2.x);
        yButton2.update(gamepad2.y);
        dPadUp2.update(gamepad2.dpad_up);
        dPadDown2.update(gamepad2.dpad_down);
        dPadLeft2.update(gamepad2.dpad_left);
        dPadRight2.update(gamepad2.dpad_right);
        bumperLeft2.update(gamepad2.left_bumper);
        bumperRight2.update(gamepad2.right_bumper);
    }
}