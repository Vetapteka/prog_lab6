package commands;

import model.MyCollection;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.List;
import java.util.Scanner;


public class ExitCommand implements Command, Serializable {

    @Override
    public void setArgs(Scanner scanner, PrintStream out, List<String> args) {

    }

    @Override
    public String execute(MyCollection myCollection) throws FileNotFoundException {

        return "the server disconnects you";
    }


    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public String getDescription() {
        return "terminate program (without saving to file)";
    }
}
