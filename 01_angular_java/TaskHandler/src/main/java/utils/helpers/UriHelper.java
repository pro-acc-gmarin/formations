package utils.helpers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

public class UriHelper {

    private static final String URI_FIRST_CHARACTER = "/";

    static public Optional<String> getFirstUriPart(final String uri){
        try {
            URI location = new URI(uri);
            String[] uriParts = location.getPath().split("/");
            if(uriParts.length > 2){
                return Optional.of(uriParts[2]);
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URI", e);
        }
        return Optional.empty();
    }

    static public URI convertStringToUri(final String segment){
        String validSegment = segment;
        if(!segment.startsWith(URI_FIRST_CHARACTER)){
            validSegment = String.format(URI_FIRST_CHARACTER+"%s", segment);
        }
        return URI.create(validSegment);
    }
}
