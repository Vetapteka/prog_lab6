package commands;

import model.Flat;
import utils.DatabaseManager;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Set;


public class ClearCommand extends Command {

    public ClearCommand() {
        super("clear", "clear the collection");
    }

    @Override
    public String execute(Hashtable<Integer, Flat> flats) throws FileNotFoundException{
        DatabaseManager.connectionToDataBase();
        try {
            Set<Integer> flatsId = DatabaseManager.getUserFlatsId(getUser());
            flats.entrySet().removeIf(e -> flatsId.contains(e.getKey()));
            DatabaseManager.deleteUserFlats(getUser());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.getSuccessMessage();
    }

}
