package commands;

import model.Flat;
import utils.Reader;

import java.io.PrintStream;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;


public abstract class ReplaceIf extends Command {

    private Integer id;
    private Flat flat;

    public ReplaceIf(String name, String description) {
        super(name, description);
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
            int compareRes = flat.compareTo(flats.get(id));
            if (checkCompareResult(compareRes)) {
                flats.replace(id, flat);
                sb.append("Change!\n");
            }
            sb.append("No change!\n");
            sb.append(this.getSuccessMessage());

        } else sb.append("no such flat");
        return sb.toString();
    }

    abstract boolean checkCompareResult(int compareResult);
}
