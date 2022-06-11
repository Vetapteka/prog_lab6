package commands;

import model.Flat;
import model.FurnishComparator;
import model.MyCollection;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;


public class PrintFiledCommand implements Command {

    public PrintFiledCommand() {
    }

    @Override
    public void setArgs(Scanner scanner, PrintStream out, List<String> args) {
    }

    @Override
    public String execute(MyCollection myCollection) throws FileNotFoundException {
        Hashtable<Integer, Flat> flats = myCollection.getCollection();
        StringBuilder sb = new StringBuilder();
        flats.keySet().stream().map(x -> flats.get(x).getFurnish()).sorted(new FurnishComparator()).forEach(sb::append);
        return sb.toString();
    }


    @Override
    public String getName() {
        return "print_field_descending_furnish";
    }

    @Override
    public String getDescription() {
        return "print the furnish field values of all elements in descending order";
    }
}
