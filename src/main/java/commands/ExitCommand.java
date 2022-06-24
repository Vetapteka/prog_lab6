package commands;

import model.MyCollection;
import utils.PropertiesManager;

public class ExitCommand extends Command {

    public ExitCommand() {
        super(PropertiesManager.getProperties().getProperty("exitCommandName"),
                "terminate program (without saving to file)");
    }

    @Override
    public String execute(MyCollection myCollection) {
        return "the server disconnects you";
    }

}
