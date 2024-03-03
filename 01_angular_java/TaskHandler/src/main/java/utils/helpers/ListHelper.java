package utils.helpers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListHelper {

    static public String joinListString(final List<String> listString){
        return String.join(";", listString);
    }

    static public List<String> splitStringToList(final String string){
        return Arrays.asList(string.split(";"));
    }

}

