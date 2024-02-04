package gateway.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private static final ConnectionPool INSTANCE = new ConnectionPool();
    private HikariConfig config = new HikariConfig();
    private HikariDataSource datasource;

    private ConnectionPool() {
    }

    public void configureHikariDataSource(DataSource datasource){
        config.setDataSource(datasource);
        this.datasource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return this.datasource.getConnection();
    }

    public static void closeDatasource(){
        INSTANCE.closeDatasource();
    }

    public static ConnectionPool getInstance(){
        return INSTANCE;
    }
}
