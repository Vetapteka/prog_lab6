package model;

import utils.Reader;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.Scanner;

/**
 * apartment coordinates
 */
public class Coordinates implements Serializable {
    /**
     * max value is 285
     */
    private final long x;
    /**
     * max field value: 76, Field cannot be null
     */
    private final Float y;

    public Coordinates(long x, Float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Initializes a newly created Coordinates object
     *
     * @param scanner input from where to read values
     * @param out     output from where to request values
     */

    public Coordinates(Scanner scanner, PrintStream out) {
        x = Reader.readNumber(Long::parseLong, scanner, out,
                "Coordinates x: ", Long.MIN_VALUE, 285).longValue();

        y = (Float) Reader.readNumber(Float::parseFloat, scanner, out,
                "Coordinates y: ", Float.MIN_VALUE, 76);
    }

    public long getX() {
        return x;
    }

    public Float getY() {
        return y;
    }

    @Override
    public String toString() {
        return "{" + " x='" + x + "'" + ", y='" + y + "'" + "}";
    }
}