package utils.helpers;

import utils.enumerations.UriControllerEnum;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class UriHelper {

    private static final String URI_FIRST_CHARACTER = "/";

    static public Optional<String> getUriServletPart(final String uri){
        final Optional<String> oUriServletPart = ofNullable(getUriPart(uri, 2));
        if(oUriServletPart.isPresent()) {
            if (Arrays.stream(UriControllerEnum.values()).anyMatch(value -> value.toString().equals(oUriServletPart.get()))) {
                return oUriServletPart;
            }
        }
        return Optional.empty();
    }

    static public Optional<String> getUriParameterPart(final String uri){
        final Optional<String> oUriParameterPart = ofNullable(getUriPart(uri, 3));
        if(oUriParameterPart.isPresent()){
            return oUriParameterPart;
        }
        return Optional.empty();
    }
    private static String getUriPart(final String uri, final int index){
        try {
            final URI location = new URI(uri);
            final String[] uriParts = location.getPath().split("/");
            if(uriParts.length > index){
                return uriParts[index];
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URI", e);
        }
        return null;
    }

    static public URI convertStringToUri(final String segment){
        String validSegment = segment;
        if(!segment.startsWith(URI_FIRST_CHARACTER)){
            validSegment = String.format(URI_FIRST_CHARACTER+"%s", segment);
        }
        return URI.create(validSegment);
    }
}
