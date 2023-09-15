package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

public class GamepadWrapper {
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
    public Gamepad gamepad;

    public GamepadWrapper(Gamepad gamepad) {
        this.gamepad = gamepad;
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
        leftStickX = joystickDeadzoneCorrection(gamepad.left_stick_x);
        leftStickY = joystickDeadzoneCorrection(-gamepad.left_stick_y);
        rightStickX = joystickDeadzoneCorrection(gamepad.right_stick_x);
        rightStickY = joystickDeadzoneCorrection(gamepad.right_stick_y);
        triggerLeft = gamepad.left_trigger;
        triggerRight = gamepad.right_trigger;
        aButton.update(gamepad.a);
        bButton.update(gamepad.b);
        xButton.update(gamepad.x);
        yButton.update(gamepad.y);
        dPadUp.update(gamepad.dpad_up);
        dPadDown.update(gamepad.dpad_down);
        dPadLeft.update(gamepad.dpad_left);
        dPadRight.update(gamepad.dpad_right);
        bumperLeft.update(gamepad.left_bumper);
        bumperRight.update(gamepad.right_bumper);
    }
}
