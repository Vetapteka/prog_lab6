package commands;

import model.Flat;
import model.MyCollection;
import utils.Reader;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class RemoveGreaterKeyCommand implements Command {
    private Integer id;

    public RemoveGreaterKeyCommand() {
    }

    @Override
    public void setArgs(Scanner scanner, PrintStream out, List<String> args)
            throws IllegalArgumentException, IndexOutOfBoundsException {
        id = (Integer) Reader.readParsebleNumber(Integer::parseInt, args.get(1));
    }

    @Override
    public String execute(MyCollection myCollection) throws FileNotFoundException {
        Hashtable<Integer, Flat> flats = myCollection.getCollection();
        List<Integer> filterList = flats.keySet().stream().filter(x -> x > id).collect(Collectors.toList());
        filterList.forEach(flats::remove);
        return successMessage;
    }

    @Override
    public String getName() {
        return "remove_greater_key";
    }

    @Override
    public String getDescription() {
        return "remove from the collection all elements whose key is greater than the given one";
    }
}
