package commands;

import model.Flat;
import utils.DatabaseManager;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Set;


public class ClearCommand extends Command {

    public ClearCommand() {
        super("clear", "clear the collection (only your flats)");
    }

    @Override
    public String execute(Hashtable<Integer, Flat> flats) {
        String res;
        try {
            Set<Integer> flatsId = DatabaseManager.getUserFlatsId(getUser());
            flats.entrySet().removeIf(e -> flatsId.contains(e.getKey()));
            DatabaseManager.deleteUserFlats(getUser());
            res = this.getSuccessMessage();
        } catch (SQLException e) {
            res = getFailMessage();
        }
        return res;
    }

}
