package commands;


import model.MyCollection;
import utils.Reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.*;

public class ExecuteCommand implements Command, Serializable {

    private final LinkedHashMap<String, Command> commands;
    private LinkedList<Command> awaitingExecutionCommands;

    public ExecuteCommand(LinkedHashMap<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public void setArgs(Scanner scanner, PrintStream out, List<String> args) throws FileNotFoundException {
        awaitingExecutionCommands = new LinkedList<>();
        File file = new File(Reader.readNotEmptyString(args.get(1)));
            Scanner scannerFile = new Scanner(file);
            while (scannerFile.hasNext()) {
                Command command = Reader.readCommand(scannerFile, new PrintStream("log.txt"), commands);
                if (command != null)
                    if (command instanceof ExitCommand) {
                        out.println("WARNING: exit command will not be executed. If you want to exit, enter the command separately\n");
                    }
                    else {
                        awaitingExecutionCommands.add(command);
                    }
            }

    }

    @Override
    public String execute(MyCollection myCollection) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        awaitingExecutionCommands.forEach(x -> {
            try {
                sb.append(x.execute(myCollection));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        return sb.toString();
    }

    @Override
    public String getName() {
        return "execute_script";
    }

    @Override
    public String getDescription() {
        return "read and execute the script from the specified file. The script contains commands in the same form in which the user enters them interactively.";

    }
}
