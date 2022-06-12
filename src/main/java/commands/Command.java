package commands;

import model.MyCollection;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public interface Command  {
    String successMessage = "done!\n";

    void setArgs(Scanner scanner, PrintStream out, List<String> args)
            throws IllegalArgumentException, FileNotFoundException;

    String execute(MyCollection myCollection) throws FileNotFoundException;

    String getName();

    String getDescription();
}
