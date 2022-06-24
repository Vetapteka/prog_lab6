package commands;

import model.MyCollection;


public class ShowCommand extends Command {

    public ShowCommand() {
        super("show", "print to standard output all elements of the collection in string representation");
    }

    @Override
    public String execute(MyCollection myCollection) {
        StringBuilder sb = new StringBuilder();
        if (!myCollection.isEmpty()) {
            myCollection.show(sb);
        } else {
            sb.append("collection is empty\n");
        }
        return sb.toString();
    }
}
