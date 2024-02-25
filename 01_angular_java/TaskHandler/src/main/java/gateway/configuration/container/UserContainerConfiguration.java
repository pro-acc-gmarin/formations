package gateway.configuration.container;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.picocontainer.MutablePicoContainer;
import user.domain.ports.api.UserServiceImpl;
import user.domain.ports.api.UserServicePort;
import user.domain.ports.spi.UserPersistencePort;
import user.infrastructure.adapter.UserRepository;
import user.infrastructure.dao.UserDao;

import javax.servlet.ServletContext;

public class UserContainerConfiguration {
    private static final Logger logger = LogManager.getLogger(UserContainerConfiguration.class);

    public static String USER_CONTAINER = "user_container";

    public static void configure(final MutablePicoContainer globalContainer, final ServletContext servletContext){
        MutablePicoContainer userContainer = globalContainer.makeChildContainer();
        addComponents(userContainer);
        servletContext.setAttribute(USER_CONTAINER, userContainer);
        logger.info(USER_CONTAINER+ " started.");
    }

    private static void addComponents(final MutablePicoContainer container){
        container.addComponent(UserPersistencePort.class, UserRepository.class)
                .addComponent(UserServicePort.class, UserServiceImpl.class)
                .addComponent(UserDao.class);
        logger.info(USER_CONTAINER+ " components added.");
    }
}
