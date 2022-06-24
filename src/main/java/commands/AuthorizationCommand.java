package commands;

import model.MyCollection;
import model.User;
import utils.DatabaseManager;
import utils.Hasher;
import utils.PropertiesManager;
import utils.Reader;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AuthorizationCommand extends Command {

    public AuthorizationCommand() {
        super("log_in", "<login> <password>");
    }

    @Override
    public void setArgs(Scanner scanner, PrintStream out, List<String> args)
            throws IllegalArgumentException, IndexOutOfBoundsException, NullPointerException {

        setUser(new User(Reader.readNotEmptyString(args.get(1)), Hasher.sha1(Reader.readNotEmptyString(args.get(2)))));
    }

    @Override
    public String execute(MyCollection myCollection) {
        String res;
        try {
            if (DatabaseManager.findUser(getUser()) != null) {
                res = PropertiesManager.getProperties().getProperty("successAuthorizMess");
            } else {
                throw new NullPointerException();
            }
        } catch (SQLException | NullPointerException e) {
            res = "invalid login or password";
        }

        return res;
    }
}
