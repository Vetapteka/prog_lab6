package commands;

import model.MyCollection;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

public class HelpCommand implements Command, Serializable {

    private final LinkedHashMap<String, Command> commands;
    private String commandsString;

    public HelpCommand(LinkedHashMap<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public void setArgs(Scanner scanner, PrintStream out, List<String> args) {

        StringBuilder sb = new StringBuilder();
        commands.forEach((k, v) -> sb.append(k).append(" - ").append(v.getDescription()).append("\n"));
        commandsString = sb.toString();
    }

    @Override
    public String execute(MyCollection myCollection) throws FileNotFoundException {
        return commandsString;
    }


    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "display help on available commands";
    }

}
