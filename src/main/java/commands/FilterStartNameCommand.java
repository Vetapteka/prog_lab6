package commands;

import model.MyCollection;
import utils.Reader;

import java.io.PrintStream;
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
    public String execute(MyCollection myCollection) {
        StringBuilder sb = new StringBuilder();
        myCollection.filterStartName(strStartName, sb);
        return sb.toString();
    }

}
