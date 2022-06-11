package commands;


public class ReplaceIfLowerCommand extends ReplaceIf {

    public ReplaceIfLowerCommand() {
    }

    @Override
    public String getName() {
        return "replace_if_lower";
    }

    @Override
    public String getDescription() {
        return "replace the value by key if the new value is less than the old one";
    }

    @Override
    boolean checkCompareResult(int compareResult) {
        return compareResult < 0;
    }
}


