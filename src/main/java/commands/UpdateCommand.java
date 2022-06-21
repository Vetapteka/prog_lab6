package commands;

import model.Flat;
import utils.Reader;

import java.io.PrintStream;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class UpdateCommand extends Command {
    private Integer id;
    private Flat flat;

    public UpdateCommand() {
        super("update", "update the value of the collection element whose id is equal to the given one");
    }

    @Override
    public void setArgs(Scanner scanner, PrintStream out, List<String> args)
            throws IllegalArgumentException, IndexOutOfBoundsException, NullPointerException {

        id = (Integer) Reader.readParsebleNumber(Integer::parseInt, args.get(1));
        flat = new Flat(scanner, id, out);
    }

    @Override
    public String execute(Hashtable<Integer, Flat> flats) {
        StringBuilder sb = new StringBuilder();
        if (flats.containsKey(id)) {
            flats.put(id, flat);
            sb.append(this.getSuccessMessage());
        } else {
            sb.append("Id not found. There are only such flats: ");
            Collections.list(flats.keys()).forEach(sb::append);
        }

        return sb.toString();
    }

}
