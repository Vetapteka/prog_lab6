package commands;

import model.Flat;
import model.MyCollection;

import java.io.FileNotFoundException;
import java.util.Hashtable;


public class InfoCommand extends Command {

    public InfoCommand() {
        super("info", "print information about the collection to the " +
                "standard output stream (type, initialization date, number of elements, etc.)");
    }

    @Override
    public String execute(MyCollection myCollection) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        Hashtable<Integer, Flat> flats = myCollection.getCollection();

        sb.append("type: ").append(flats.getClass().toString()).append("\n");
        sb.append("initialization date: ").append(myCollection.getInitialization_date().toString()).append("\n");
        sb.append("info of elements: ").append(flats.size()).append("\n");

        return sb.toString();
    }

}
