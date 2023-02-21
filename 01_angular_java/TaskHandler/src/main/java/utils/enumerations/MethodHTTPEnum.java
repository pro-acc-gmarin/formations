package utils.enumerations;

import java.util.Arrays;
import java.util.Optional;

public enum MethodHTTPEnum {
    GET("get"),
    POST("post"),
    UPDATE("update"),
    DELETE("delete"),
    PUT("put");

    private final String label;
    MethodHTTPEnum(String label) {
        this.label = label;
    }

    public static Optional<MethodHTTPEnum> fromString(String value){
        return Arrays.stream(values()).filter(currentEnum -> currentEnum.name().equals(value)).findFirst();
    }

    @Override
    public String toString() {
        return label;
    }
}
