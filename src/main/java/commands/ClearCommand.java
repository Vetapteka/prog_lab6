package commands;

import model.MyCollection;
import utils.DatabaseManager;

import java.sql.SQLException;
import java.util.Set;


public class ClearCommand extends Command {

    public ClearCommand() {
        super("clear", "clear the collection (only your flats)");
    }

    @Override
    public String execute(MyCollection myCollection) {
        String res;
        try {
            Set<Integer> flatsId = DatabaseManager.getUserFlatsId(getUser());
            myCollection.clear(flatsId);
            DatabaseManager.deleteUserFlats(getUser());
            res = this.getSuccessMessage();
        } catch (SQLException e) {
            res = getFailMessage();
        }
        return res;
    }

}
