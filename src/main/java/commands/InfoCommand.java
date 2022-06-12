package commands;

import model.Flat;
import model.MyCollection;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;


public class InfoCommand implements Command, Serializable {

    public InfoCommand() {
    }

    @Override
    public void setArgs(Scanner scanner, PrintStream out, List<String> args) {
    }

    @Override
    public String execute(MyCollection myCollection) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        Hashtable<Integer, Flat> flats = myCollection.getCollection();

        sb.append("type: ").append(flats.getClass().toString()).append("\n");
        sb.append("initialization date: ").append(myCollection.getInitialization_date().toString()).append("\n");
        sb.append("info of elements: ").append(flats.size()).append("\n");

        return sb.toString();
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "print information about the collection to the standard output stream (type, initialization date, number of elements, etc.)";
    }
}
