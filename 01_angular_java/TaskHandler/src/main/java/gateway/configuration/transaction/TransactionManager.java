package gateway.configuration.transaction;

import java.sql.Connection;

public class TransactionManager {

    private static final ThreadLocal<Connection> context = new ThreadLocal<>();

    public static void setConnection(final Connection connection) {
        context.set(connection);
    }

    public static Connection getConnection() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }
}
