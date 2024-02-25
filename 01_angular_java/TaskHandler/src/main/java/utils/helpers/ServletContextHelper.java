package utils.helpers;

import utils.enumerations.ServletContextKey;

import javax.servlet.ServletContext;

public class ServletContextHelper {

    public static void setAttribute(final ServletContext context, final ServletContextKey key, final Object value){
        context.setAttribute(key.name(), value);
    }

    public static Object getAttribute(final ServletContext context, final ServletContextKey key){
        return context.getAttribute(key.name());
    }
}
