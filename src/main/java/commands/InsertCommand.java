package commands;

import model.Flat;
import model.MyCollection;
import utils.Reader;

import java.io.PrintStream;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class InsertCommand implements Command {
    Integer id;
    Flat flat;

    public InsertCommand() {
    }

    @Override
    public void setArgs(Scanner scanner, PrintStream out, List<String> args)
            throws IllegalArgumentException, IndexOutOfBoundsException {
        id = (Integer) Reader.readParsebleNumber(Integer::parseInt, args.get(1));
        flat = new Flat(scanner, id, out);
    }

    @Override
    public String execute(MyCollection myCollection) {
        StringBuilder sb = new StringBuilder();
        Hashtable<Integer, Flat> flats = myCollection.getCollection();
        if (flats.containsKey(id)) {
            sb.append("the key already exists");
        } else {
            flats.put(id, flat);
            sb.append(successMessage);
        }
        return sb.toString();
    }

    @Override
    public String getName() {
        return "insert";
    }

    @Override
    public String getDescription() {
        return "add new element with given key";
    }
}
