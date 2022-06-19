import commands.*;
import utils.PropertiesManager;
import utils.Reader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Scanner;

public class Client {
    private static final Scanner scanner = new Scanner(System.in);
    private static final PrintStream out = System.out;
    private static final Properties properties = PropertiesManager.getProperties();

    public static void main(String[] args) throws IOException {

        SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 5454));
        ByteBuffer buffer = ByteBuffer.allocate(256000);
        LinkedHashMap<String, Command> commands = initCommands();

        Command command = null;
        do {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            try {
                command = Reader.readCommand(scanner, out, commands);
                oos.writeObject(command);
                oos.flush();


                buffer.clear();
                buffer.put(baos.toByteArray());
                baos.flush();
                buffer.flip();
                client.write(buffer);
                buffer.clear();
                client.read(buffer);
                buffer.flip();

                byte[] response = new byte[buffer.limit()];
                System.arraycopy(buffer.array(), 0, response, 0, buffer.limit());
                System.out.print(new String(response));

                oos.close();
                baos.close();

            } catch (NullPointerException e) {
                out.println("Invalid argument");

            }
        } while (command == null || !command.getName().equals(properties.getProperty("exitCommandName")));

        client.close();
    }


    private static LinkedHashMap<String, Command> initCommands() {
        LinkedHashMap<String, Command> commands = new LinkedHashMap<>();

        HelpCommand help = new HelpCommand(commands);
        ExecuteCommand execute = new ExecuteCommand(commands);
        InfoCommand info = new InfoCommand();
        ExitCommand exit = new ExitCommand();
        InsertCommand insert = new InsertCommand();
        RemoveCommand remove = new RemoveCommand();
        ClearCommand clear = new ClearCommand();
        UpdateCommand update = new UpdateCommand();
        CountByHouseCommand countByHouseCommand = new CountByHouseCommand();
        ShowCommand show = new ShowCommand();
        PrintFiledCommand printFiled = new PrintFiledCommand();
        RemoveGreaterKeyCommand removeGreaterKeyCommand = new RemoveGreaterKeyCommand();
        ReplaceIfGraterCommand replaceIfGrater = new ReplaceIfGraterCommand();
        ReplaceIfLowerCommand replaceIfLower = new ReplaceIfLowerCommand();
        FilterStartNameCommand filterStartName = new FilterStartNameCommand();

        commands.put(execute.getName(), execute);
        commands.put(help.getName(), help);
        commands.put(info.getName(), info);
        commands.put(exit.getName(), exit);
        commands.put(insert.getName(), insert);
        commands.put(remove.getName(), remove);
        commands.put(clear.getName(), clear);
        commands.put(update.getName(), update);
        commands.put(countByHouseCommand.getName(), countByHouseCommand);
        commands.put(show.getName(), show);
        commands.put(printFiled.getName(), printFiled);
        commands.put(removeGreaterKeyCommand.getName(), removeGreaterKeyCommand);
        commands.put(replaceIfGrater.getName(), replaceIfGrater);
        commands.put(replaceIfLower.getName(), replaceIfLower);
        commands.put(filterStartName.getName(), filterStartName);

        return commands;
    }

}
