package commands;

import model.MyCollection;
import model.User;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.List;
import java.util.Scanner;

public abstract class Command implements Serializable {
    private final String name;
    private final String description;
    private User user;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setArgs(Scanner scanner, PrintStream out, List<String> args)
            throws IllegalArgumentException, FileNotFoundException {
    }

    public abstract String execute(MyCollection myCollection);

    public String getSuccessMessage() {
        return "done!\n";
    }

    public String getFailMessage() {
        return "fail! (smth wrong with database)\n";
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
