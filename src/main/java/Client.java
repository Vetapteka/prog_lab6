import commands.*;
import model.User;
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
    private static final String serverDisconnectMess = "server side error";
    private static final PrintStream out = System.out;
    private static final Properties properties = PropertiesManager.getProperties();
    private static final LinkedHashMap<String, Command> commands = new LinkedHashMap<>();


    public static void main(String[] args) {


        SocketChannel client = null;
        try {
            client = SocketChannel.open(new InetSocketAddress("localhost", 5454));
        } catch (IOException e) {
            System.out.println(serverDisconnectMess);
            System.exit(1);
        }
        ByteBuffer buffer = ByteBuffer.allocate(256000);

        initStartCommands();
        Command command = null;
        do {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(baos);
            } catch (IOException e) {
                e.printStackTrace();
            }

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

                byte[] responseByte = new byte[buffer.limit()];
                System.arraycopy(buffer.array(), 0, responseByte, 0, buffer.limit());
                String response = new String(responseByte);
                System.out.print(response);

                oos.close();
                baos.close();

                if (command instanceof AuthorizationCommand && response
                        .equals(PropertiesManager.getProperties().getProperty("successAuthorizMess"))) {
                    initAllCommands(command.getUser());

                }

            } catch (NullPointerException  e) {
                out.println("Invalid argument");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (command == null || !command.getName().equals(properties.getProperty("exitCommandName")));

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initStartCommands() {
        RegistrationCommand register = new RegistrationCommand();
        HelpCommand help = new HelpCommand(commands);
        ExitCommand exit = new ExitCommand();
        AuthorizationCommand logIn = new AuthorizationCommand();


        commands.put(register.getName(), register);
        commands.put(help.getName(), help);
        commands.put(exit.getName(), exit);
        commands.put(logIn.getName(), logIn);
    }

    private static void initAllCommands(User user) {

        ExecuteCommand execute = new ExecuteCommand(commands);
        InfoCommand info = new InfoCommand();
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
        commands.put(info.getName(), info);
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

        commands.values().forEach(x -> x.setUser(user));
    }

}
