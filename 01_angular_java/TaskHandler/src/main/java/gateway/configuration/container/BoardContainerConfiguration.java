package gateway.configuration.container;

import board.domain.ports.api.BoardServiceImpl;
import board.domain.ports.api.BoardServicePort;
import board.domain.ports.spi.BoardPersistencePort;
import board.infrastructure.adapter.BoardRepository;
import board.infrastructure.dao.BoardDao;
import board.infrastructure.spi.BoardDaoSpi;
import gateway.configuration.transaction.TransactionProxyFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.picocontainer.MutablePicoContainer;
import utils.helpers.ServletContextHelper;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import static utils.enumerations.ServletContextKey.BOARD_CONTAINER;


public class BoardContainerConfiguration {

    private static final Logger logger = LogManager.getLogger(BoardContainerConfiguration.class);

    public static void configure(final MutablePicoContainer globalContainer, final ServletContext servletContext){
        MutablePicoContainer boardContainer = globalContainer.makeChildContainer();
        addComponents(boardContainer);
        addProxies(boardContainer, globalContainer);
        ServletContextHelper.setAttribute(servletContext, BOARD_CONTAINER, boardContainer);
        logger.info(BOARD_CONTAINER+ " started.");
    }

    private static void addComponents(final MutablePicoContainer container){
        container.addComponent(BoardPersistencePort.class, BoardRepository.class)
                .addComponent(BoardServicePort.class, BoardServiceImpl.class);
        logger.info(BOARD_CONTAINER+ " components added.");
    }

    private static void addProxies(final MutablePicoContainer container, final MutablePicoContainer globalContainer){
        BoardDaoSpi boardDao = new BoardDao();
        BoardDaoSpi boardDaoProxy = TransactionProxyFactory.createProxy(boardDao, globalContainer.getComponent(DataSource.class));
        container.addComponent(BoardDaoSpi.class, boardDaoProxy);
        logger.info(BOARD_CONTAINER+ " proxies added.");
    }
}
