package utils;


import commands.Command;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.function.Function;


public class Reader {

    public static Command readCommand(Scanner scanner, PrintStream out, LinkedHashMap<String, Command> commands) {
        Command command = null;
        out.print("\n> ");
        while (scanner.hasNext()) {
            String str = scanner.nextLine();
            String commandName = str.split(" ")[0];

            if (commands.containsKey(commandName)) {
                command = commands.get(commandName);
                try {
                    command.setArgs(scanner, out, Arrays.asList(str.split(" ")));
                } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
                    out.println("Invalid arguments");
                    out.print("\n> ");
                    continue;
                } catch (FileNotFoundException e) {
                    out.println("The file does not open or the file does not exist");
                    out.print("\n> ");

                    continue;
                }
                break;
            } else {
                out.println("No such command " + commandName.split(" ")[0]);
                out.print("\n> ");

            }
        }
        return command;
    }

    public static Number readNumber(Function<String, Number> parser, Scanner scanner, PrintStream out, String startStr, Number min, Number max) throws NullPointerException {
        Number res = null;
        String str;
        scanner.useDelimiter("\r");

        out.print(startStr);
        do {
            try {
                res = readParsebleNumber(parser, scanner.nextLine());
            } catch (IllegalArgumentException e) {
                out.println("enter the number in the range " + min + ".." + max);
                out.print(startStr);
                continue;
            }

            if (res.floatValue() < min.floatValue() || res.floatValue() > max.floatValue()) {
                out.println("the value must be in the range " + min + ".." + max);
                out.print(startStr);
                continue;
            }
            break;
        } while (scanner.hasNext());

        if (res == null) throw new NullPointerException();

        return res;
    }

    public static String readString(Scanner scanner, PrintStream out, String startStr) throws NullPointerException {
        String res = null;
        scanner.useDelimiter("\r");

        out.print(startStr);

        do {
            try {
                res = readNotEmptyString(scanner.nextLine());
            } catch (IllegalArgumentException e) {
                out.println("The string cannot be empty");
                out.print(startStr);
                continue;
            }
            break;
        } while (scanner.hasNext());

        if (res == null) throw new NullPointerException();
        return res;
    }

    public static <T extends Enum<T>> T readEnum(Class<T> tClass, Scanner scanner, PrintStream out, boolean canBeNull) throws NullPointerException {
        T res = null;
        String str;
        scanner.useDelimiter("\r");

        String startStr = tClass.getSimpleName() + " (" + Arrays.stream(tClass.getEnumConstants()).map(Object::toString).reduce((x, y) -> x + " | " + y).orElse("") + "): ";
        out.print(startStr);

        do {
            str = scanner.nextLine().toUpperCase();
            if (str.equals("") && canBeNull) {
                str = "NONE";
            }
            try {
                res = T.valueOf(tClass, str);
                break;
            } catch (IllegalArgumentException e) {
                out.println("invalid value");
                out.print(startStr);
            }
        } while (scanner.hasNext());

        if (res == null) throw new NullPointerException();
        return res;
    }

    public static String readNotEmptyString(String str) throws IllegalArgumentException {
        if (!(str.equals(""))) {
            return str;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static Number readParsebleNumber(Function<String, Number> parser, String str) throws IllegalArgumentException {
        Number res;
        try {
            res = parser.apply(str);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        return res;
    }

    public static String readFileName(String arg) {

        String delimiter = "\\\\";
        String[] substr;
        StringBuilder res = new StringBuilder();
        substr = arg.split(delimiter);

        for (int i = 0; i < substr.length - 1; i++) {
            res.append(substr[i]).append(delimiter).append(delimiter);
        }
        res.append(substr[substr.length - 1]);

        return res.toString();
    }
}
