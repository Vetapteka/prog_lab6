package commands;

import model.Flat;

import java.util.Hashtable;


public class ShowCommand extends Command {

    public ShowCommand() {
        super("show", "print to standard output all elements of the collection in string representation");
    }

    @Override
    public String execute(Hashtable<Integer, Flat> flats) {
        StringBuilder sb = new StringBuilder();
        if (!flats.isEmpty()) {
            flats.keySet().stream().map(flats::get).forEach(x -> sb.append(x.toString()).append("\n"));
        } else {
            sb.append("collection is empty\n");
        }
        return sb.toString();
    }
}
