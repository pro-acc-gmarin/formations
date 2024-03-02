package gateway.configuration.transaction;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;

public class TransactionProxyFactory {

    public static <T> T createProxy(final T target, final DataSource dataSource) {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new TransactionMethodInterceptor(target, dataSource)
        );
    }
}
