package commands;

import model.Flat;
import model.MyCollection;
import utils.DatabaseManager;
import utils.Reader;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


public abstract class Replace extends Command {

    private Integer id;
    private Flat flat;

    public Replace(String name, String description) {
        super(name, description);
    }

    @Override
    public void setArgs(Scanner scanner, PrintStream out, List<String> args)
            throws IllegalArgumentException, IndexOutOfBoundsException, NullPointerException {

        id = (Integer) Reader.readParsebleNumber(Integer::parseInt, args.get(1));
        flat = new Flat(scanner, id, out);

    }

    @Override
    public String execute(MyCollection myCollection) {
        StringBuilder sb = new StringBuilder();

        if (myCollection.containsKey(id)) {
            int compareRes = flat.compareTo(myCollection.get(id));
            if (checkCompareResult(compareRes)) {

                try {
                    Set<Integer> flatsId = DatabaseManager.getUserFlatsId(getUser());
                    if (flatsId.contains(id)) {
                        DatabaseManager.updateFlat(getUser(), flat);
                        myCollection.replace(id, flat);
                        sb.append(getSuccessMessage());
                    } else {
                        sb.append("it's not yours\n");
                    }

                } catch (SQLException e) {
                    sb.append(getFailMessage());
                }
            }
        } else sb.append("no such flat");
        return sb.toString();
    }

    abstract boolean checkCompareResult(int compareResult);
}
