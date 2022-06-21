package commands;

import model.MyCollection;
import utils.DatabaseManager;
import utils.Hasher;
import utils.PropertiesManager;
import utils.Reader;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AuthorizationCommand extends Command {

    private String login;
    private String password;

    public AuthorizationCommand() {
        super("log_in", "<login> <password>");
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
        DatabaseManager.connectionToDataBase();
        try {
            Integer userId = DatabaseManager.findUser(login, password);
//          TODO загрузить с этим id myColltction и если там null в id, то бросить исключение
            res = PropertiesManager.getProperties().getProperty("successAuthorizMess");
        } catch (SQLException | NullPointerException e) {
            res = "invalid login or password";
        }
        return res;
    }

}
