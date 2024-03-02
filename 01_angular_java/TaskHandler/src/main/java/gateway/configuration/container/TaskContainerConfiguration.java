package gateway.configuration.container;

import gateway.configuration.transaction.TransactionProxyFactory;
import gateway.utils.LogsHelper;
import org.picocontainer.MutablePicoContainer;
import task.domain.ports.api.TaskServiceImpl;
import task.domain.ports.api.TaskServicePort;
import task.domain.ports.spi.TaskPersistencePort;
import task.infrastructure.adapter.TaskRepository;
import task.infrastructure.dao.TaskDao;
import task.infrastructure.spi.TaskDaoSpi;
import utils.helpers.ServletContextHelper;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import static utils.enumerations.ServletContextKey.TASK_CONTAINER;

public class TaskContainerConfiguration {

    public static void configure(final MutablePicoContainer globalContainer, final ServletContext servletContext) {
        MutablePicoContainer taskContainer = globalContainer.makeChildContainer();
        addComponents(taskContainer);
        addProxies(taskContainer, globalContainer);
        ServletContextHelper.setAttribute(servletContext, TASK_CONTAINER, taskContainer);
        LogsHelper.info(TASK_CONTAINER + " start.");
    }

    private static void addComponents(final MutablePicoContainer container) {
        container.addComponent(TaskPersistencePort.class, TaskRepository.class)
                .addComponent(TaskServicePort.class, TaskServiceImpl.class);
        LogsHelper.info(TASK_CONTAINER + " components added.");
    }

    private static void addProxies(final MutablePicoContainer container, final MutablePicoContainer globalContainer) {
        TaskDaoSpi taskDao = new TaskDao();
        TaskDaoSpi taskDaoProxy = TransactionProxyFactory.createProxy(taskDao, globalContainer.getComponent(DataSource.class));
        container.addComponent(TaskDaoSpi.class, taskDaoProxy);
        LogsHelper.info(TASK_CONTAINER + " proxies added.");
    }
}
