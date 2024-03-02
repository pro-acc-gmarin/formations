package gateway.configuration.transaction;

import gateway.utils.LogsHelper;
import utils.annotations.Transactional;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;

public class TransactionMethodInterceptor implements InvocationHandler {
    private final Object target;
    private final DataSource dataSource;

    public TransactionMethodInterceptor(final Object target, final DataSource dataSource) {
        this.target = target;
        this.dataSource = dataSource;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        LogsHelper.info("Dao Method Interceptor - Start");
        final Method targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
        if (targetMethod.isAnnotationPresent(Transactional.class)) {
            final Connection connection = dataSource.getConnection();
            TransactionManager.setConnection(connection);
            try {
                connection.setAutoCommit(false);
                final Object result = method.invoke(target, args);
                connection.commit();

                LogsHelper.info("Transaction is commited.");
                return result;
            } catch (InvocationTargetException e) {
                connection.rollback();
                LogsHelper.info("Transaction is rollbacked.");
                throw e.getTargetException();
            } finally {
                connection.close();
                TransactionManager.clear();
                LogsHelper.info("Dao Method Interceptor - End");
            }
        } else {
            LogsHelper.info("Dao Method not transactional.");
            return method.invoke(target, args);
        }
    }
}
