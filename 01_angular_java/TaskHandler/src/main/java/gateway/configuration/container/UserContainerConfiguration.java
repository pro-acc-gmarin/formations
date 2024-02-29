package gateway.configuration.container;

import gateway.configuration.transaction.TransactionProxyFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger(UserContainerConfiguration.class);


    public static void configure(final MutablePicoContainer globalContainer, final ServletContext servletContext){
        MutablePicoContainer userContainer = globalContainer.makeChildContainer();
        addComponents(userContainer);
        addProxies(userContainer, globalContainer);
        ServletContextHelper.setAttribute(servletContext, USER_CONTAINER, userContainer);
        logger.info(USER_CONTAINER+ " started.");
    }

    private static void addComponents(final MutablePicoContainer container){
        container.addComponent(UserPersistencePort.class, UserRepository.class)
                .addComponent(UserServicePort.class, UserServiceImpl.class);
        logger.info(USER_CONTAINER+ " components added.");
    }

    private static void addProxies(final MutablePicoContainer container, final MutablePicoContainer globalContainer){
        UserDaoSpi userDao = new UserDao();
        UserDaoSpi userDaoProxy = TransactionProxyFactory.createProxy(userDao, globalContainer.getComponent(DataSource.class));
        container.addComponent(UserDaoSpi.class, userDaoProxy);
        logger.info(TASK_CONTAINER+ " proxies added.");
    }
}
