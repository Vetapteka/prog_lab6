package commands;

import model.Flat;

import java.io.FileNotFoundException;
import java.util.Hashtable;


public class InfoCommand extends Command {

    public InfoCommand() {
        super("info", "print information about the collection to the " +
                "standard output stream (type, initialization date, number of elements, etc.)");
    }

    @Override
    public String execute(Hashtable<Integer, Flat> flats) throws FileNotFoundException {
        return "type: " + flats.getClass().toString() + "\n" +
                "info of elements: " + flats.size() + "\n";
    }

}
