package util;

import java.awt.*;

/**
 * Color utility class
 */
public class ColorUtil {

    /**
     * Converts a color string ('red' | 'white' | 'brown' | 'black') to java.awt.Color
     *
     * @param color color as string
     * @return Color
     */
    public static Color toColor(String color) {
        switch (color) {
            case "red":
            case "\"red\"":
                return Color.RED;
            case "white":
            case "\"white\"":
                return Color.WHITE;
            case "brown":
            case "\"brown\"":
                return new Color(210, 105, 30);
            case "black":
            case "\"black\"":
                return Color.BLACK;
            default:
                throw new IllegalArgumentException("Invalid color given: " + color);
        }
    }

    /**
     * Converts a color string ('red' | 'white' | 'brown' | 'black') to java.awt.Color
     *
     * @param color color as string
     * @return Color
     */
    public static String toColorString(Color color) {
        switch (color.toString()) {
            case "java.awt.Color[r=255,g=0,b=0]":
                return "red";
            case "java.awt.Color[r=255,g=255,b=255]":
                return "white";
            case "java.awt.Color[r=210,g=105,b=30]":
                return "brown";
            case "java.awt.Color[r=0,g=0,b=0]":
                return "black";
            default:
                throw new IllegalArgumentException("Invalid color given");
        }
    }
}
