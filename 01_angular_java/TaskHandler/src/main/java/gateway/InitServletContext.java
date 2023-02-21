package gateway;

import gateway.configuration.DataSourceConfiguration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

public class InitServletContext implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        this.dataSourceRegistering(servletContext);
    }
    private void dataSourceRegistering(final ServletContext servletContext){
        DataSource dataSource = DataSourceConfiguration.getDataSource();
        servletContext.setAttribute("datasource", dataSource);
    }
}
