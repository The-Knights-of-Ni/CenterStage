package org.firstinspires.ftc.teamcode;

import android.util.Log;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Util.AllianceColor;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.HashMap;

import static org.mockito.Mockito.*;

public class RunTest {
    Robot init() {
        HardwareMap hardwareMap = mock(HardwareMap.class);
        hardwareMap.dcMotor = (HardwareMap.DeviceMapping<DcMotor>) mock(HardwareMap.DeviceMapping.class);
        when(hardwareMap.dcMotor.get("fl")).thenReturn(new MockDcMotorEx());
        when(hardwareMap.dcMotor.get("fr")).thenReturn(new MockDcMotorEx());
        when(hardwareMap.dcMotor.get("rl")).thenReturn(new MockDcMotorEx());
        when(hardwareMap.dcMotor.get("rr")).thenReturn(new MockDcMotorEx());
        HashMap<String, Boolean> flags = new HashMap<>();
        flags.put("vision", false);
        return new Robot(hardwareMap, new MockTelemetry(), new ElapsedTime(), AllianceColor.BLUE,
                new Gamepad(), new Gamepad(), flags);
    }

    @Test
    void testStart() {
        try (MockedStatic<Log> mocked = mockStatic(Log.class)) {
            Robot robot = init();
        }
    }
}
