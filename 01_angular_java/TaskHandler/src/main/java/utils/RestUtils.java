package utils;

import utils.enumerations.RestContextEnum;
import utils.helpers.ListHelper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RestUtils {

    static public List<String> splitStringToHypermediaList(final RestContextEnum context, final String string){
        return ListHelper.splitStringToList(string).stream().map(currentId -> createHypermediaLink(context.context(), string)).collect(Collectors.toList());
    }

    static public String createHypermediaLink(final String context, final String id){
        return context + "/" +id;
    }
}
