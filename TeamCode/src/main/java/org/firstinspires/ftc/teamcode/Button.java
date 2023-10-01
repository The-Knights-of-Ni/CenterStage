package org.firstinspires.ftc.teamcode;

public class Button {
    public ButtonStatus status;
    public boolean toggle = false;

    public Button() {
        status = ButtonStatus.NOTCLICKED;
    }

    public void update(boolean value) {
        switch (status) {
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
    }

    public boolean isPressed() {
        return status == ButtonStatus.CLICKING;
    }

    public boolean hasPressedPrev() {
        return status == ButtonStatus.CLICKED;
    }
}
