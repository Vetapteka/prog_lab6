package commands;

import model.Flat;
import utils.PropertiesManager;

import java.util.Hashtable;

public class ExitCommand extends Command {

    public ExitCommand() {
        super(PropertiesManager.getProperties().getProperty("exitCommandName"),
                "terminate program (without saving to file)");
    }

    @Override
    public String execute(Hashtable<Integer, Flat> flats) {
        return "the server disconnects you";
    }

}
