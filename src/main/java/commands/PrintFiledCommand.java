package commands;

import model.Flat;
import model.FurnishComparator;

import java.util.Hashtable;


public class PrintFiledCommand extends Command {

    public PrintFiledCommand() {
        super("print_field_descending_furnish", "print the furnish field" +
                " values of all elements in descending order");
    }

    @Override
    public String execute(Hashtable<Integer, Flat> flats) {
        StringBuilder sb = new StringBuilder();
        flats.keySet().stream().map(x -> flats.get(x).getFurnish()).sorted(new FurnishComparator()).forEach(sb::append);
        return sb.toString();
    }

}
