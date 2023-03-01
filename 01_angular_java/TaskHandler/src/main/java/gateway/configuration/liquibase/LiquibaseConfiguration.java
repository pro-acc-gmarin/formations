package gateway.configuration.liquibase;

import gateway.configuration.DataSourceConfiguration;

public class LiquibaseConfiguration extends LiquibaseServletContext{

    @Override
    public void setDatasForProperties() {
        this.setDataSource(DataSourceConfiguration.getDataSource());
        this.setDataSourcePath("java:/comp/env/jdbc/AppTHDB");
        this.setChangeLogPath("classpath:/db/changelog.yaml");
        this.setPassword("root");
    }
}
