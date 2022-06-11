package commands;

import model.Flat;
import model.MyCollection;
import utils.Reader;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;


public class RemoveCommand implements Command {
   private Integer id;

    public RemoveCommand() {
    }

    @Override
    public void setArgs(Scanner scanner, PrintStream out, List<String> args)
            throws IllegalArgumentException, IndexOutOfBoundsException {
        id = (Integer) Reader.readParsebleNumber(Integer::parseInt, args.get(1));
    }

    @Override
    public String execute(MyCollection myCollection) throws FileNotFoundException {
        Hashtable<Integer, Flat> flats = myCollection.getCollection();
        flats.remove(id);
        return successMessage;
    }


    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "remove an element from a collection by its key";
    }
}
