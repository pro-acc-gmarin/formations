package gateway.configuration.container;

import board.domain.ports.api.BoardServiceImpl;
import board.domain.ports.api.BoardServicePort;
import board.domain.ports.spi.BoardPersistencePort;
import board.infrastructure.adapter.BoardRepository;
import board.infrastructure.dao.BoardDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.picocontainer.MutablePicoContainer;

import javax.servlet.ServletContext;


public class BoardContainerConfiguration {

    private static final Logger logger = LogManager.getLogger(BoardContainerConfiguration.class);

    public static String BOARD_CONTAINER = "board_container";

    public static void configure(final MutablePicoContainer globalContainer, final ServletContext servletContext){
        MutablePicoContainer boardContainer = globalContainer.makeChildContainer();
        addComponents(globalContainer);
        //boardContainer.start();
        servletContext.setAttribute(BOARD_CONTAINER, boardContainer);
        logger.info(BOARD_CONTAINER+ " started.");
    }

    private static void addComponents(final MutablePicoContainer container){
        container.addComponent(BoardPersistencePort.class, BoardRepository.class)
                .addComponent(BoardServicePort.class, BoardServiceImpl.class)
                .addComponent(BoardDao.class);
        logger.info(BOARD_CONTAINER+ " components added.");
    }

    public static void destroy(final ServletContext servletContext){
        final MutablePicoContainer boardContainer = (MutablePicoContainer) servletContext.getAttribute(BOARD_CONTAINER);
        boardContainer.stop();
        logger.info(BOARD_CONTAINER+ " stopped.");
    }
}
