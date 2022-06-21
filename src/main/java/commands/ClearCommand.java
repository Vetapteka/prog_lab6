package commands;

import model.Flat;
import model.MyCollection;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;


public class ClearCommand extends Command {

    public ClearCommand() {
        super("clear", "clear the collection");
    }

    @Override
    public String execute(MyCollection myCollection) throws FileNotFoundException {
        Hashtable<Integer, Flat> flats = myCollection.getCollection();
        flats.clear();
        return this.getSuccessMessage();
    }

}
