package commands;

public class UpdateCommand extends Replace {


    public UpdateCommand() {
        super("update", "update the value of the collection element whose id is equal to the given one");
    }

    @Override
    boolean checkCompareResult(int compareResult) {
        return true;
    }
}
