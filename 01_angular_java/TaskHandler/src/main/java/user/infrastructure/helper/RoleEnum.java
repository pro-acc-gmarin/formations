package user.infrastructure.helper;

import java.util.Arrays;
import java.util.Optional;

public enum RoleEnum {
    STANDARD("standard"),
    ADMINISTRATOR("administrator");

    private final String label;
    RoleEnum(final String label) {
        this.label = label;
    }

    public static Optional<RoleEnum> fromString(final String value){
        return Arrays.stream(values()).filter(currentEnum -> currentEnum.name().equals(value)).findFirst();
    }

    @Override
    public String toString() {
        return label;
    }
}
