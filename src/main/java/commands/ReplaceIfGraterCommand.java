package commands;

public class ReplaceIfGraterCommand extends ReplaceIf {

    public ReplaceIfGraterCommand() {
        super("replace_if_greater", "replace value by key if new value is greater than old");
    }

    @Override
    boolean checkCompareResult(int compareResult) {
        return compareResult > 0;
    }
}
