package gateway;

import gateway.configuration.ConnectionPool;
import gateway.configuration.DataSourceConfiguration;
import gateway.configuration.Log4j2Configuration;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.helpers.LoggerHelper;
import utils.metric.PrometheusMetricRegistry;

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
        this.connectionPoolRegistering(servletContext);
        LoggerHelper.logInfo(LOGGER, LoggerHelper.GATEWAY, "Context servlet initialization succeed");
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
