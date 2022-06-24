package commands;

import model.FurnishComparator;
import model.MyCollection;


public class PrintFiledCommand extends Command {

    public PrintFiledCommand() {
        super("print_field_descending_furnish", "print the furnish field" +
                " values of all elements in descending order");
    }

    @Override
    public String execute(MyCollection myCollection) {
        StringBuilder sb = new StringBuilder();
        myCollection.printField(sb);
        return sb.toString();
    }

}
