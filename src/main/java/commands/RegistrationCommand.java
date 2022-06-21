package commands;

import model.Flat;
import model.User;
import utils.DatabaseManager;
import utils.Hasher;
import utils.Reader;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class RegistrationCommand extends Command {

    private String login;
    private String password;

    public RegistrationCommand() {
        super("register", "[register] <login> <password>");
    }

    @Override
    public void setArgs(Scanner scanner, PrintStream out, List<String> args)
            throws IllegalArgumentException, IndexOutOfBoundsException, NullPointerException {
        login = Reader.readNotEmptyString(args.get(1));
        password = Hasher.sha1(Reader.readNotEmptyString(args.get(2)));
    }

    @Override
    public String execute(Hashtable<Integer, Flat> flats) {
        String res;
        try {
            DatabaseManager.registerUser(new User(login, password));
            res = this.getSuccessMessage();
        } catch (SQLException e) {
            res = "user already exists";
        }
        return res;
    }

}
