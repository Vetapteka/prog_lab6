package commands;

import model.Flat;
import model.MyCollection;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;


public class ShowCommand implements Command, Serializable {

    public ShowCommand() {
    }

    @Override
    public void setArgs(Scanner scanner, PrintStream out, List<String> args) throws IllegalArgumentException, FileNotFoundException {
    }

    @Override
    public String execute(MyCollection myCollection) {
        StringBuilder sb = new StringBuilder();
        Hashtable<Integer, Flat> flats = myCollection.getCollection();
        flats.keySet().stream().map(flats::get).forEach(x -> sb.append(x.toString()).append("\n"));
        return sb.toString();
    }

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getDescription() {
        return "print to standard output all elements of the collection in string representation";

    }
}
