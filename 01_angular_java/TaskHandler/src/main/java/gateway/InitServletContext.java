package gateway;

import gateway.configuration.ConnectionPool;
import gateway.configuration.DataSourceConfiguration;
import gateway.configuration.Log4j2Configuration;
import gateway.configuration.container.GlobalContainerConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.helpers.LoggerHelper;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

public class InitServletContext implements ServletContextListener {

    private static final Logger LOGGER = LogManager.getLogger(InitServletContext.class);

    private String ATTRIBUTE_DATASOURCE = "datasource";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        Log4j2Configuration.setConfigLocation();
        this.dataSourceRegistering(servletContext);
        GlobalContainerConfiguration.configure(servletContext);
        this.connectionPoolRegistering(servletContext);
        LoggerHelper.logInfo(LOGGER, LoggerHelper.GATEWAY, "Context servlet initialization succeed");
    }
    @Override
    public void contextDestroyed(final ServletContextEvent servletContextEvent) {
        final ServletContext servletContext = servletContextEvent.getServletContext();
        GlobalContainerConfiguration.destroy(servletContext);
        ConnectionPool.closeDatasource();
    }

    private void dataSourceRegistering(final ServletContext servletContext){
        DataSource dataSource = DataSourceConfiguration.getDataSource();
        servletContext.setAttribute(ATTRIBUTE_DATASOURCE, dataSource);
    }

    private void connectionPoolRegistering(final ServletContext servletContext){
        DataSource datasource = (DataSource) servletContext.getAttribute(ATTRIBUTE_DATASOURCE);
        ConnectionPool.getInstance().configureHikariDataSource(datasource);
    }
}
