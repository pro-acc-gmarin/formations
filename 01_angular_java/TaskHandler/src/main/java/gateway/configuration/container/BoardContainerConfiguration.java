package gateway.configuration.container;

import board.domain.ports.api.BoardServiceImpl;
import board.domain.ports.api.BoardServicePort;
import board.domain.ports.spi.BoardPersistencePort;
import board.infrastructure.adapter.BoardRepository;
import board.infrastructure.dao.BoardDao;
import board.infrastructure.spi.BoardDaoSpi;
import gateway.configuration.transaction.TransactionProxyFactory;
import gateway.utils.LogsHelper;
import org.picocontainer.MutablePicoContainer;
import utils.helpers.ServletContextHelper;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import static utils.enumerations.ServletContextKey.BOARD_CONTAINER;


public class BoardContainerConfiguration {

    public static void configure(final MutablePicoContainer globalContainer, final ServletContext servletContext) {
        final MutablePicoContainer boardContainer = globalContainer.makeChildContainer();
        addComponents(boardContainer);
        addProxies(boardContainer, globalContainer);
        ServletContextHelper.setAttribute(servletContext, BOARD_CONTAINER, boardContainer);
        LogsHelper.info(BOARD_CONTAINER + " started.");
    }

    private static void addComponents(final MutablePicoContainer container) {
        container.addComponent(BoardPersistencePort.class, BoardRepository.class)
                .addComponent(BoardServicePort.class, BoardServiceImpl.class);
        LogsHelper.info(BOARD_CONTAINER + " components added.");
    }

    private static void addProxies(final MutablePicoContainer container, final MutablePicoContainer globalContainer) {
        final BoardDaoSpi boardDao = new BoardDao();
        final BoardDaoSpi boardDaoProxy = TransactionProxyFactory.createProxy(boardDao, globalContainer.getComponent(DataSource.class));
        container.addComponent(BoardDaoSpi.class, boardDaoProxy);
        LogsHelper.info(BOARD_CONTAINER + " proxies added.");
    }
}
