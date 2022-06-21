package commands;

import model.Flat;
import model.MyCollection;
import utils.Reader;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;


public class FilterStartNameCommand extends Command {
    private String strStartName;

    public FilterStartNameCommand() {
        super("filter_starts_with_name",
                "display elements whose name field value starts with the given substring");
    }

    @Override
    public void setArgs(Scanner scanner, PrintStream out, List<String> args) throws IllegalArgumentException, IndexOutOfBoundsException {
        strStartName = Reader.readNotEmptyString(args.get(1));
    }

    @Override
    public String execute(MyCollection myCollection) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        Hashtable<Integer, Flat> flats = myCollection.getCollection();
        flats.keySet().stream().map(flats::get).filter(x -> x.getName().startsWith(strStartName)).forEach(sb::append);
        return sb.toString();
    }

}
