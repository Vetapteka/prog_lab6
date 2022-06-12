package commands;

import model.Flat;
import model.MyCollection;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;


public class ClearCommand implements Command, Serializable {

    public ClearCommand() {
    }

    @Override
    public void setArgs(Scanner scanner, PrintStream out, List<String> args) {
    }

    @Override
    public String execute(MyCollection myCollection) throws FileNotFoundException {
        Hashtable<Integer, Flat> flats = myCollection.getCollection();
        flats.clear();
        return successMessage;
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "clear the collection";
    }

}
