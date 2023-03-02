package gateway.configuration.liquibase;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import liquibase.integration.servlet.LiquibaseServletListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static java.util.Optional.ofNullable;

public abstract class LiquibaseServletContext extends LiquibaseServletListener {


    private static final Logger logger = LogManager.getLogger(LiquibaseServletContext.class);

    private DataSource dataSource;
    private String dataSourcePath;
    private String changeLogPath;
    private String password;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            this.setDatasForProperties();
            this.initProperties();
            // Call parent contextInitialized() method to run Liquibase
            super.contextInitialized(servletContextEvent);
        } catch (Exception e) {

            throw new RuntimeException("Failed to initialize Liquibase!", e);
        }
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Liquibase servlet context destroying - START");
        super.contextDestroyed(sce);
        AbandonedConnectionCleanupThread.checkedShutdown();
        logger.info("Liquibase servlet context destroying - END");
    }

    public void setDataSource(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setDataSourcePath(String dataSourcePath){
        this.dataSourcePath = dataSourcePath;
    }

    public void setChangeLogPath(String changeLogPath){
        this.changeLogPath = changeLogPath;
    }

    private void initProperties() throws SQLException {
        if(ofNullable(this.dataSource).isPresent()) {
            DatabaseMetaData databaseMetaData = this.dataSource.getConnection().getMetaData();
            System.setProperty("liquibase.driver", databaseMetaData.getDriverName());
            System.setProperty("liquibase.url", databaseMetaData.getURL());
            System.setProperty("liquibase.username", databaseMetaData.getUserName());
        }
        System.setProperty("liquibase.datasource", this.dataSourcePath);
        System.setProperty("liquibase.changelog", this.changeLogPath);

        System.setProperty("liquibase.password", this.password);
    }

    public abstract void setDatasForProperties();

}