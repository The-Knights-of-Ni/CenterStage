package org.firstinspires.ftc.teamcode.DriveControl;

import org.firstinspires.ftc.teamcode.Util.Coordinate;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class PathFile {
    public static final int VERSION = 0;

    public static byte getCheckSum(ArrayList<Byte> array) {
        int sum = 0;
        int checkSum = 0;
        for (byte b: array) {
            sum += b;
        }
        checkSum = sum%256;
        return (byte) checkSum;
    }

    public static Path read(File file) throws IOException {
        FileInputStream fs = new FileInputStream(file);
        ArrayList<Integer> contents = new ArrayList<>();
        ArrayList<Integer> all = new ArrayList<>();
        int r;
        while((r=fs.read())!=-1) {
            contents.add(r);
            all.add(r);
        }
        if (contents.get(0) > VERSION)
            throw new UnsupportedEncodingException("Unsupported Version of pathfile Current Version: " + VERSION + " File Version: " + contents.get(0));
        contents.remove(0);
        ArrayList<Waypoint> waypointArrayList = new ArrayList<>();
        int numberOfInfoBlocks = contents.get(0);
        int numberOfInfoBlocksLeft = numberOfInfoBlocks;
        contents.remove(0);
        int numOfX = contents.get(0);
        contents.remove(0);
        numberOfInfoBlocksLeft--;
        int numOfY = contents.get(0);
        contents.remove(0);
        numberOfInfoBlocksLeft--;
        String checkSum;
        if (numberOfInfoBlocks>2) {
            checkSum = contents.get(0).toString();
            contents.remove(0);
            numberOfInfoBlocksLeft--;
        }
        else {
            checkSum = "";
        }
        contents.subList(0, numberOfInfoBlocksLeft).clear();
        if (!Objects.equals(checkSum, "")) {
            int sum = 0;
            for (int i: all) {
                sum += i;
            }
            sum -= Integer.parseInt(checkSum);
            if (sum % 256 != Integer.parseInt(checkSum)) {
                throw new StreamCorruptedException("FileStream was corrupt, checksum not equal to actual sum");
            }
        }
        int counter = 0;
        boolean stop;
        int x = 0;
        int y = 0;
        int chunkSize = numOfX + numOfY + 1;
        for (int i:contents) {
            int which = counter % chunkSize;
            if (which<numOfX) {
                x += i;
            }
            else if (which == (chunkSize-1)) {
                stop = i % 2 == 1;
                waypointArrayList.add(new Waypoint(new Coordinate(x, y), stop));
                x = 0;
                y = 0;
            }
            else {
                y += i;
            }
            counter++;
        }
        return new Path(waypointArrayList);
    }

    public static void write(File file, Path path) throws IOException {
        FileOutputStream fo = new FileOutputStream(file);
        ArrayList<Byte> arrayOut = new ArrayList<>();
        arrayOut.add((byte) VERSION); // Version is the first byte
        arrayOut.add((byte) 3); // length of header is the second
        byte xMax = 32;
        byte yMax = 32;
        // TODO: Find the actual numbers
        arrayOut.add(xMax); // bytes per x
        arrayOut.add(yMax); // bytes per y
        arrayOut.add((byte) 0); // Checksum
        for (Waypoint waypoint:path.waypoints) {
            int x = (int) waypoint.coordinate.getX();
            int y = (int) waypoint.coordinate.getY();
            int counter = 0;
            while (x>255) {
                arrayOut.add((byte) 255);
                x -= 255;
                counter++;
            }
            arrayOut.add((byte) x);
            counter++;

            if (xMax < counter) {
                throw new RuntimeException("x is too large. x="+ waypoint.coordinate.getX() + " xMax="+(xMax*255) + " counter=" + counter);
            }
            else {
                while (counter<xMax) {
                    arrayOut.add((byte) 0);
                    counter++;
                }
            }

            counter = 0;
            while (y > 255) {
                arrayOut.add((byte) 255);
                y -= 255;
                counter++;
            }
            arrayOut.add((byte) y);
            counter++;

            if (yMax < counter) {
                throw new RuntimeException("y is too large. y="+ waypoint.coordinate.getY() + " yMax="+(yMax*255) + " counter=" + counter);
            }
            else {
                while (counter<yMax) {
                    arrayOut.add((byte) 0);
                    counter++;
                }
            }


            if (waypoint.fullStop) {
                arrayOut.add((byte) 1);
            }
            else {
                arrayOut.add((byte) 0);
            }
        }
        arrayOut.set(4, getCheckSum(arrayOut));
        // Turn the arrayList into an array
        byte[] output = new byte[arrayOut.size()];
        int count = 0;
        for (byte b: arrayOut) {
            output[count] = b;
            count++;
        }
        fo.write(output);
    }
}
