package commands;

import model.MyCollection;
import utils.DatabaseHandler;
import utils.Hasher;
import utils.Reader;

import java.io.PrintStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AuthorizationCommand implements Command, Serializable {

    private String login;
    private String password;

    public AuthorizationCommand() {
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
            Integer userId = DatabaseHandler.findUser(login, password);
//          TODO загрузить с этим id myColltction и если там null в id, то бросить исключение
            res = successMessage;
        } catch (SQLException | NullPointerException e) {
            res = "invalid login or password";
        }
        return res;
    }

    @Override
    public String getName() {
        return "log_in";
    }

    @Override
    public String getDescription() {
        return "<login> <password>";
    }
}
