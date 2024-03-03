package utils.enumerations;

public enum RestContextEnum {
    BOARD("board"),
    TASK("task");

    private final String context;

    RestContextEnum(final String context){
        this.context = context;
    }

    public String context(){
        return this.context;
    }
}
