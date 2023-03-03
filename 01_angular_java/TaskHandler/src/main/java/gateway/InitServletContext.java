package gateway;

import gateway.configuration.ConnectionPool;
import gateway.configuration.DataSourceConfiguration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

public class InitServletContext implements ServletContextListener {

    private String ATTRIBUTE_DATASOURCE = "datasource";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        this.dataSourceRegistering(servletContext);
        this.connectionPoolRegistering(servletContext);
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
