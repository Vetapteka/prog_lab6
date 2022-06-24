package commands;

import model.MyCollection;


public class InfoCommand extends Command {

    public InfoCommand() {
        super("info", "print information about the collection to the " +
                "standard output stream (type, initialization date, number of elements, etc.)");
    }

    @Override
    public String execute(MyCollection myCollection) {
        return myCollection.info();
    }

}
