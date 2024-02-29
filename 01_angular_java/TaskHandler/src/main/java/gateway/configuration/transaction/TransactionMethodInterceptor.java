package gateway.configuration.transaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.annotations.Transactional;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;

public class TransactionMethodInterceptor implements InvocationHandler {

    private static final Logger logger = LogManager.getLogger(TransactionMethodInterceptor.class);
    private final Object target;
    private final DataSource dataSource;

    public TransactionMethodInterceptor(final Object target, final DataSource dataSource){
        this.target = target;
        this.dataSource = dataSource;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("Dao Method Interceptor - Start");
       final Method targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
        if (targetMethod.isAnnotationPresent(Transactional.class)) {
            Connection connection = dataSource.getConnection();
            TransactionManager.setConnection(connection);
            try {
                connection.setAutoCommit(false);
                Object result = method.invoke(target, args);
                connection.commit();

                logger.info("Transaction is commited.");
                return result;
            } catch (InvocationTargetException e) {
                connection.rollback();
                logger.info("Transaction is rollbacked.");
                throw e.getTargetException();
            } finally {
                connection.close();
                TransactionManager.clear();
                logger.info("Dao Method Interceptor - End");
            }
        } else {
            logger.info("Dao Method not transactional.");
            return method.invoke(target, args);
        }
    }
}
