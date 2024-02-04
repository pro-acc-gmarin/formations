package gateway.configuration.container;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;

import javax.servlet.ServletContext;


public class GlobalContainerConfiguration {
    private static final Logger logger = LogManager.getLogger(GlobalContainerConfiguration.class);

    public static String GLOBAL_CONTAINER = "global_container";

    public static void configure(final ServletContext servletContext){
        final MutablePicoContainer globalContainer = new DefaultPicoContainer();
        BoardContainerConfiguration.configure(globalContainer, servletContext);
        /*UserContainerConfiguration.configure(globalContainer, servletContext);
        TaskContainerConfiguration.configure(globalContainer, servletContext);*/
        globalContainer.start();
        servletContext.setAttribute(GLOBAL_CONTAINER, globalContainer);
        logger.info(GLOBAL_CONTAINER+ " started.");
    }

    public static void destroy(final ServletContext servletContext){
        final MutablePicoContainer globalContainer = (MutablePicoContainer) servletContext.getAttribute(GLOBAL_CONTAINER);
        /*BoardContainerConfiguration.destroy(servletContext);
        UserContainerConfiguration.destroy(servletContext);
        TaskContainerConfiguration.destroy(servletContext);*/
        globalContainer.stop();
        logger.info(GLOBAL_CONTAINER+ " stopped.");
    }
}
