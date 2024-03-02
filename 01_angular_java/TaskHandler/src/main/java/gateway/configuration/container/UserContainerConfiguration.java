package gateway.configuration.container;

import gateway.configuration.transaction.TransactionProxyFactory;
import gateway.utils.LogsHelper;
import org.picocontainer.MutablePicoContainer;
import user.domain.ports.api.UserServiceImpl;
import user.domain.ports.api.UserServicePort;
import user.domain.ports.spi.UserPersistencePort;
import user.infrastructure.adapter.UserRepository;
import user.infrastructure.dao.UserDao;
import user.infrastructure.spi.UserDaoSpi;
import utils.helpers.ServletContextHelper;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import static utils.enumerations.ServletContextKey.TASK_CONTAINER;
import static utils.enumerations.ServletContextKey.USER_CONTAINER;

public class UserContainerConfiguration {

    public static void configure(final MutablePicoContainer globalContainer, final ServletContext servletContext) {
        final MutablePicoContainer userContainer = globalContainer.makeChildContainer();
        addComponents(userContainer);
        addProxies(userContainer, globalContainer);
        ServletContextHelper.setAttribute(servletContext, USER_CONTAINER, userContainer);
        LogsHelper.info(USER_CONTAINER + " started.");
    }

    private static void addComponents(final MutablePicoContainer container) {
        container.addComponent(UserPersistencePort.class, UserRepository.class)
                .addComponent(UserServicePort.class, UserServiceImpl.class);
        LogsHelper.info(USER_CONTAINER + " components added.");
    }

    private static void addProxies(final MutablePicoContainer container, final MutablePicoContainer globalContainer) {
        final UserDaoSpi userDao = new UserDao();
        final UserDaoSpi userDaoProxy = TransactionProxyFactory.createProxy(userDao, globalContainer.getComponent(DataSource.class));
        container.addComponent(UserDaoSpi.class, userDaoProxy);
        LogsHelper.info(TASK_CONTAINER + " proxies added.");
    }
}
