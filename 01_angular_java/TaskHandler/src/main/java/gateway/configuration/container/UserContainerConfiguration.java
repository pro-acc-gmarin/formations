package gateway.configuration.container;

import lombok.extern.slf4j.Slf4j;
import org.picocontainer.MutablePicoContainer;

import javax.servlet.ServletContext;

@Slf4j
public class UserContainerConfiguration {

    public static String USER_CONTAINER = "user_container";

    public static void configure(final MutablePicoContainer globalContainer, final ServletContext servletContext){
        MutablePicoContainer userContainer = globalContainer.makeChildContainer();

        userContainer.start();
        servletContext.setAttribute(USER_CONTAINER, userContainer);
        log.info(USER_CONTAINER+ " started.");
    }

    public static void destroy(final ServletContext servletContext){
        final MutablePicoContainer userContainer = (MutablePicoContainer) servletContext.getAttribute(USER_CONTAINER);
        userContainer.stop();
        log.info(USER_CONTAINER+ " stopped.");
    }
}
