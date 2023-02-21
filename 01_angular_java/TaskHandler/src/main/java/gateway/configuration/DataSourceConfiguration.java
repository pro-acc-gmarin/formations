package gateway.configuration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DataSourceConfiguration {

    public static DataSource getDataSource(){
        try {
            Context ctx = new InitialContext();
            Context initCtx  = (Context) ctx.lookup("java:/comp/env");
            return (DataSource) initCtx.lookup("jdbc/AppTHDB");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}