package utils.enumerations;

import java.util.Arrays;
import java.util.Optional;

public enum ServletNameEnum {
    USER_SERVLET("UserServlet"),
    BOARD_SERVLET("BoardServlet"),
    TASK_SERVLET("TaskServlet"),
    LOGIN_SERVLET("LoginServlet");

    private final String label;
    ServletNameEnum(String label) {
        this.label = label;
    }

    public static Optional<ServletNameEnum> fromString(String value){
        return Arrays.stream(values()).filter(currentEnum -> currentEnum.name().equals(value)).findFirst();
    }

    @Override
    public String toString() {
        return label;
    }
}
