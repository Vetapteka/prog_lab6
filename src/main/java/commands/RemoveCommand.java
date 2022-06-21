package commands;

import model.Flat;
import utils.Reader;

import java.io.PrintStream;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;


public class RemoveCommand extends Command {
    private Integer id;

    public RemoveCommand() {
        super("remove", "remove an element from a collection by its key");
    }

    @Override
    public void setArgs(Scanner scanner, PrintStream out, List<String> args)
            throws IllegalArgumentException, IndexOutOfBoundsException {
        id = (Integer) Reader.readParsebleNumber(Integer::parseInt, args.get(1));
    }

    @Override
    public String execute(Hashtable<Integer, Flat> flats) {

        flats.remove(id);
        return this.getSuccessMessage();
    }

}
