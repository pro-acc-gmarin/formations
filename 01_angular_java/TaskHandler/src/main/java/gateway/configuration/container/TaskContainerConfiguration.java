package gateway.configuration.container;

import lombok.extern.slf4j.Slf4j;
import org.picocontainer.MutablePicoContainer;

import javax.servlet.ServletContext;

@Slf4j
public class TaskContainerConfiguration {

    public static String TASK_CONTAINER = "task_container";

    public static void configure(final MutablePicoContainer globalContainer, final ServletContext servletContext){
        MutablePicoContainer taskContainer = globalContainer.makeChildContainer();

        taskContainer.start();
        servletContext.setAttribute(TASK_CONTAINER, taskContainer);
        log.info(TASK_CONTAINER+ " start.");
    }

    public static void destroy(final ServletContext servletContext){
        final MutablePicoContainer taskContainer = (MutablePicoContainer) servletContext.getAttribute(TASK_CONTAINER);
        taskContainer.stop();
        log.info(TASK_CONTAINER+ " stopped.");
    }
}
