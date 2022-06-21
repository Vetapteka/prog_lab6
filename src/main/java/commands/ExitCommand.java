package commands;

import model.MyCollection;
import utils.PropertiesManager;

import java.io.*;

public class ExitCommand extends Command {

    public ExitCommand() {
        super(PropertiesManager.getProperties().getProperty("exitCommandName"),
                "terminate program (without saving to file)");
    }

    @Override
    public String execute(MyCollection myCollection) throws FileNotFoundException {

        return "the server disconnects you";
    }

}
