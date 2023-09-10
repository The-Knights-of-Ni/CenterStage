package org.firstinspires.ftc.teamcode.Subsystems.Vision;

import org.firstinspires.ftc.teamcode.DriveControl.BoundingBox;
import org.firstinspires.ftc.teamcode.Util.Vector;

public class VisionCorrectionThreadData {
    private static VisionCorrectionThreadData vtd;
    private static Vector correctionVector;

    private static BoundingBox theoreticalPosition;
    private static boolean close = false;

    public static VisionCorrectionThreadData getVTD() {
        return vtd;
    }

    public static Vector getCorrectionVector() {
        return correctionVector;
    }

    public void setCorrectionVector(Vector correctionVector) {
        vtd.correctionVector = correctionVector;
    }

    public boolean getClose() {
        return close;
    }

    public void setClose(boolean newClose) {
        vtd.close = newClose;
    }

    public BoundingBox getTheoreticalPosition() {
        return theoreticalPosition;
    }

    public void setTheoreticalPosition(BoundingBox newPosition) {
        vtd.theoreticalPosition = newPosition;
    }
}
