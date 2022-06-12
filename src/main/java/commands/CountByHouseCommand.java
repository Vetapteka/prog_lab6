package commands;

import model.Flat;
import model.House;
import model.MyCollection;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class CountByHouseCommand implements Command , Serializable {
    private House house;

    public CountByHouseCommand() {
    }

    @Override
    public void setArgs(Scanner scanner, PrintStream out, List<String> args) {
        house = new House(scanner, out);
    }

    @Override
    public String execute(MyCollection myCollection) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        Hashtable<Integer, Flat> flats = myCollection.getCollection();

        long count = flats.values().stream().map(Flat::getHouse)
                .filter(x -> x != null && x.compare(house)).count();
        sb.append("There are ").append(count).append(" such houses.");
        return sb.toString();
    }

    @Override
    public String getName() {
        return "count_by_house";
    }


    @Override
    public String getDescription() {
        return "display the number of elements whose house field value is equal to the given one";
    }

}