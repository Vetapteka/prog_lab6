package commands;


public class ReplaceIfLowerCommand extends ReplaceIf {

    public ReplaceIfLowerCommand() {
        super("replace_if_lower", "replace the value by key if the new value is less than the old one");
    }

    @Override
    boolean checkCompareResult(int compareResult) {
        return compareResult < 0;
    }
}


