package gateway.configuration.container;

import com.zaxxer.hikari.HikariDataSource;
import gateway.utils.LogsHelper;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import utils.helpers.ServletContextHelper;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import static utils.enumerations.ServletContextKey.DATASOURCE;
import static utils.enumerations.ServletContextKey.GLOBAL_CONTAINER;


public class GlobalContainerConfiguration {

    public static void configure(final ServletContext servletContext) {
        final MutablePicoContainer globalContainer = new DefaultPicoContainer();
        addComponents(globalContainer, servletContext);

        BoardContainerConfiguration.configure(globalContainer, servletContext);
        UserContainerConfiguration.configure(globalContainer, servletContext);
        TaskContainerConfiguration.configure(globalContainer, servletContext);

        globalContainer.start();
        ServletContextHelper.setAttribute(servletContext, GLOBAL_CONTAINER, globalContainer);
        LogsHelper.info(GLOBAL_CONTAINER + " started.");
    }

    private static void addComponents(final MutablePicoContainer container, final ServletContext servletContext) {
        final HikariDataSource hikariDataSource = (HikariDataSource) ServletContextHelper.getAttribute(servletContext, DATASOURCE);
        container.addComponent(DataSource.class, hikariDataSource);
        LogsHelper.info(GLOBAL_CONTAINER + " components added.");
    }

    public static void destroy(final ServletContext servletContext) {
        final MutablePicoContainer globalContainer = (MutablePicoContainer) ServletContextHelper.getAttribute(servletContext, GLOBAL_CONTAINER);
        globalContainer.stop();
        LogsHelper.info(GLOBAL_CONTAINER + " stopped.");
    }
}
