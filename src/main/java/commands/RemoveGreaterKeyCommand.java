package commands;

import model.Flat;
import utils.Reader;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class RemoveGreaterKeyCommand extends Command {
    private Integer id;

    public RemoveGreaterKeyCommand() {
        super("remove_greater_key", "remove from the collection " +
                "all elements whose key is greater than the given one");
    }

    @Override
    public void setArgs(Scanner scanner, PrintStream out, List<String> args)
            throws IllegalArgumentException, IndexOutOfBoundsException {
        id = (Integer) Reader.readParsebleNumber(Integer::parseInt, args.get(1));
    }

    @Override
    public String execute(Hashtable<Integer, Flat> flats) throws FileNotFoundException {
        List<Integer> filterList = flats.keySet().stream().filter(x -> x > id).collect(Collectors.toList());
//        TODO сюда добавить команду  remove
        filterList.forEach(flats::remove);
        return this.getSuccessMessage();
    }

}
