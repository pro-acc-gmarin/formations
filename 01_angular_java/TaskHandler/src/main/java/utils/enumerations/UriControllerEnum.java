package utils.enumerations;

import java.util.Arrays;
import java.util.Optional;

public enum UriControllerEnum {
    USER("user"),
    TASK("task"),
    BOARD("board"),
    LOGIN("login");

    private final String label;
    UriControllerEnum(final String label) {
        this.label = label;
    }

    public static Optional<UriControllerEnum> fromString(final String value){
        return Arrays.stream(values()).filter(currentEnum -> currentEnum.name().equals(value)).findFirst();
    }

    @Override
    public String toString() {
        return label;
    }
}
