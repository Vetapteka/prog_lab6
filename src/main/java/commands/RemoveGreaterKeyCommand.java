package commands;

import model.MyCollection;
import utils.DatabaseManager;
import utils.Reader;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.*;

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
    public String execute(MyCollection myCollection) {
        String res;
        try {
            Set<Integer> flatsId = DatabaseManager.getUserFlatsId(getUser());

            //get user's flatId > entered value
            flatsId.removeIf(x -> x < id);
            flatsId.forEach(x -> {
                try {
                    DatabaseManager.deleteFlat(getUser(), x);
                } catch (SQLException ignored) {
                }
            });
            flatsId.forEach(myCollection::remove);
            res = this.getSuccessMessage();
        } catch (SQLException e) {
            res = getFailMessage();
        }

        return res;

    }

}
