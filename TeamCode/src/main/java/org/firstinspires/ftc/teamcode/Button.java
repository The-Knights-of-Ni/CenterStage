package org.firstinspires.ftc.teamcode;

public class Button {
    private ButtonStatus status;
    public ButtonStatus previousStatus;
    public boolean toggle = false;

    public Button() {
        status = ButtonStatus.NOTCLICKED;
    }

    public void update(boolean value) {
        var s = status;
        switch (s) {
            case NOTCLICKED:
                if (value) {
                    status = ButtonStatus.CLICKING;
                }
            case CLICKING:
                if (!value) {
                    status = ButtonStatus.CLICKED;
                }
            case CLICKED:
                if (value) {
                    status = ButtonStatus.CLICKING;
                } else {
                    status = ButtonStatus.NOTCLICKED;
                    toggle = !toggle;
                }
        }
        previousStatus = s;
    }

    public boolean isPressed() {
        return status == ButtonStatus.CLICKING && previousStatus != ButtonStatus.CLICKING;
    }

    public boolean hasPressedPrev() {
        return status == ButtonStatus.CLICKED;
    }
}
