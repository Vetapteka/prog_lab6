package commands;

import model.Flat;
import model.MyCollection;
import utils.DatabaseManager;
import utils.Reader;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class InsertCommand extends Command {
    Integer id;
    Flat flat;

    public InsertCommand() {
        super("insert", "add new element with given key");
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
            sb.append("the key already exists");
        } else {
            myCollection.put(id, flat);
            try {

                sb.append(this.getSuccessMessage()).append("new flat with id: ").append(DatabaseManager.insertFlat(flat, this.getUser()));

            } catch (SQLException e) {
                sb.append(getFailMessage());
            }
        }
        return sb.toString();
    }

}
