package commands;

import model.MyCollection;
import utils.DatabaseManager;
import utils.Reader;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


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
    public String execute(MyCollection myCollection) {
        String res;

        try {
            Set<Integer> flatsId = DatabaseManager.getUserFlatsId(getUser());
            if (flatsId.contains(id)) {
                DatabaseManager.deleteFlat(getUser(), id);
                myCollection.remove(id);
                res = this.getSuccessMessage();
            } else {
                res = "no such flat or it's not your";
            }

        } catch (SQLException e) {
            res = getFailMessage();
        }

        return res;
    }

}
