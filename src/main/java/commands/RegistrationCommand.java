package commands;

import model.MyCollection;
import utils.DatabaseHandler;
import utils.Hasher;
import utils.PropertiesManager;
import utils.Reader;

import java.io.PrintStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class RegistrationCommand implements Command, Serializable {

    private String login;
    private String password;

    public RegistrationCommand() {
    }

    @Override
    public void setArgs(Scanner scanner, PrintStream out, List<String> args)
            throws IllegalArgumentException, IndexOutOfBoundsException, NullPointerException {
        login = Reader.readNotEmptyString(args.get(1));
        password = Hasher.sha1(Reader.readNotEmptyString(args.get(2)));
    }

    @Override
    public String execute(MyCollection myCollection) {
        String res;
        DatabaseHandler.connectionToDataBase();
        try {
            DatabaseHandler.registerUser(login, password);
            res = PropertiesManager.getProperties().getProperty("successAuthorizMess");
        } catch (SQLException e) {
            res = "user already exists";
        }
        return res;
    }

    @Override
    public String getName() {
        return "register";
    }

    @Override
    public String getDescription() {
        return "<login> <password>";
    }
}
