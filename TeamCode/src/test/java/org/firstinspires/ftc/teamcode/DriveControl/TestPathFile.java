package org.firstinspires.ftc.teamcode.DriveControl;

import org.firstinspires.ftc.teamcode.Util.Coordinate;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPathFile {
    @Test
    void testGetCheckSum() throws Exception {
        ArrayList<Byte> mockFile = new ArrayList<>();
        mockFile.add((byte) 165);
        mockFile.add((byte) 0);
        mockFile.add((byte) 6);
        mockFile.add((byte) 5);
        byte check = PathFile.getCheckSum(mockFile);
        mockFile.set(1, check);
        int sum = 0;
        for (byte b: mockFile) {
            sum += b;
        }
        sum -= check;
        assertEquals(check, (byte) sum%256);
    }
    @Test
    void testWrite() throws Exception {
        File file = new File(System.getProperty("user.dir") + "test.path");
        ArrayList<Waypoint> stops = new ArrayList<>();
        stops.add(new Waypoint(new Coordinate(0,0), true));
        stops.add(new Waypoint(new Coordinate(1000, 1250), false));
        stops.add(new Waypoint(new Coordinate(2000, 750), false));
        Path path = new Path(stops);
        try {
            PathFile.write(file, path);
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Test
    void testRead() throws Exception {
        File file = new File(System.getProperty("user.dir") + "test.path");
        Path path = PathFile.read(file);
        assertEquals(0, path.waypoints.get(0).coordinate.getX());
        assertEquals(0, path.waypoints.get(0).coordinate.getY());
        assertEquals(1000, path.waypoints.get(1).coordinate.getX());
        assertEquals(1250, path.waypoints.get(1).coordinate.getY());
        assertEquals(2000, path.waypoints.get(2).coordinate.getX());
        assertEquals(750, path.waypoints.get(2).coordinate.getY());
    }
}
