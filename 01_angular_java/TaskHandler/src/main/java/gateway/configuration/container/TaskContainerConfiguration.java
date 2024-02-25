package gateway.configuration.container;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.picocontainer.MutablePicoContainer;
import task.domain.ports.api.TaskServiceImpl;
import task.domain.ports.api.TaskServicePort;
import task.domain.ports.spi.TaskPersistencePort;
import task.infrastructure.adapter.TaskRepository;
import task.infrastructure.dao.TaskDao;

import javax.servlet.ServletContext;

public class TaskContainerConfiguration {
    private static final Logger logger = LogManager.getLogger(TaskContainerConfiguration.class);

    public static String TASK_CONTAINER = "task_container";

    public static void configure(final MutablePicoContainer globalContainer, final ServletContext servletContext){
        MutablePicoContainer taskContainer = globalContainer.makeChildContainer();
        addComponents(taskContainer);
        servletContext.setAttribute(TASK_CONTAINER, taskContainer);
        logger.info(TASK_CONTAINER+ " start.");
    }

    private static void addComponents(final MutablePicoContainer container){
        container.addComponent(TaskPersistencePort.class, TaskRepository.class)
                .addComponent(TaskServicePort.class, TaskServiceImpl.class)
                .addComponent(TaskDao.class);
        logger.info(TASK_CONTAINER+ " components added.");
    }
}
