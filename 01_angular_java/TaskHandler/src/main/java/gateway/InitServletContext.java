package gateway;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gateway.configuration.DataSourceConfiguration;
import gateway.configuration.Log4j2Configuration;
import gateway.configuration.container.GlobalContainerConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.helpers.LoggerHelper;
import utils.helpers.ServletContextHelper;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import static utils.enumerations.ServletContextKey.DATASOURCE;

public class InitServletContext implements ServletContextListener {

    private static final Logger LOGGER = LogManager.getLogger(InitServletContext.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        Log4j2Configuration.setConfigLocation();
        this.connectionPoolRegistering(servletContext);
        GlobalContainerConfiguration.configure(servletContext);
        LoggerHelper.logInfo(LOGGER, LoggerHelper.GATEWAY, "Context servlet initialization succeed");
    }
    @Override
    public void contextDestroyed(final ServletContextEvent servletContextEvent) {
        final ServletContext servletContext = servletContextEvent.getServletContext();
        GlobalContainerConfiguration.destroy(servletContext);
        final HikariDataSource hikariDataSource = (HikariDataSource) ServletContextHelper.getAttribute(servletContext, DATASOURCE);
        hikariDataSource.close();
    }

    private void connectionPoolRegistering(final ServletContext servletContext){
        final DataSource dataSource = DataSourceConfiguration.getDataSource();
        final HikariConfig config = new HikariConfig();
        config.setDataSource(dataSource);
        final HikariDataSource hikariDataSource = new HikariDataSource(config);
        ServletContextHelper.setAttribute(servletContext, DATASOURCE, hikariDataSource);
    }
}
