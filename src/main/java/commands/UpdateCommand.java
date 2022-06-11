package commands;

import model.Flat;
import model.MyCollection;
import utils.Reader;

import java.io.PrintStream;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class UpdateCommand implements Command {
    private Integer id;
    private Flat flat;

    public UpdateCommand() {
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
            flats.put(id, flat);
            sb.append(successMessage);
        } else {
            sb.append("Id not found. There are only such flats: ");
            Collections.list(flats.keys()).forEach(sb::append);
        }

        return sb.toString();
    }


    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getDescription() {
        return "update the value of the collection element whose id is equal to the given one";
    }
}
