package commands;

import model.Flat;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

public class HelpCommand extends Command {

    private final LinkedHashMap<String, Command> commands;
    private String commandsString;

    public HelpCommand(LinkedHashMap<String, Command> commands) {
        super("help", "display help on available commands");
        this.commands = commands;
    }

    @Override
    public void setArgs(Scanner scanner, PrintStream out, List<String> args) {

        StringBuilder sb = new StringBuilder();
        commands.forEach((k, v) -> sb.append(k).append(" - ").append(v.getDescription()).append("\n"));
        commandsString = sb.toString();
    }

    @Override
    public String execute(Hashtable<Integer, Flat> flats) throws FileNotFoundException {
        return commandsString;
    }

}
