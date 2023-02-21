package utils.helpers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListHelper {

    static public String joinListString(List<String> listString){
        return listString.stream().collect(Collectors.joining(";"));
    }

    static public List<String> splitStringToList(String string){
        return Arrays.asList(string.split(";"));
    }
}

