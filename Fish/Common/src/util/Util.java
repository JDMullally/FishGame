package util;

import java.awt.Point;
import java.util.ArrayList;

public class Util {


    public static boolean isPointInsideHexagon(float[] vertexX, float[] vertexY, float testX, float testY) {
        boolean c = false;
        for (int i = 0, j = 6 - 1; i < 6; j = i++)
        {
            if (((vertexY[i] > testY) != (vertexY[j] > testY))
                && (testX < (vertexX[j] - vertexX[i]) *
                (testY - vertexY[i]) / (vertexY[j] - vertexY[i]) + vertexX[i]))
                c = !c;
        }
        return c;
    }

    public static ArrayList<Point> generateHexagon(int startX, int startY, int size) {
        ArrayList<Point> hexagon = new ArrayList<>();
        hexagon.add(new Point(startX, startY));
        hexagon.add(new Point(startY+size, startX+size));
        return hexagon;
    }
}
