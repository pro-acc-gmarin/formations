package utils.aspect;

import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import utils.metric.PrometheusMetricRegistry;

@Aspect
public class PerformanceAspect {

    @Around(value = "execution(* board.infrastructure.dao.BoardDao.*(..)) && @annotation(utils.annotations.MeasurePerformance)")
    public Object measurePersistenceBoardExecutionTime(final ProceedingJoinPoint joinPoint) throws Throwable {
        final String methodName = joinPoint.getSignature().getName();
        final Timer.Sample sample = Timer.start(PrometheusMetricRegistry.getInstance().getRegistry());
        try {
            Object result = joinPoint.proceed();
            return result;
        } finally {
            sample.stop(Timer.builder(methodName + ".board.dao.execution.time")
                    .description("Time taken to execute DAO method " + methodName)
                    .tag("method", "board." + methodName)
                    .register(PrometheusMetricRegistry.getInstance().getRegistry()));
        }
    }

    @Around(value = "execution(* task.infrastructure.dao.TaskDao.*(..)) && @annotation(utils.annotations.MeasurePerformance)")
    public Object measurePersistenceTaskExecutionTime(final ProceedingJoinPoint joinPoint) throws Throwable {
        final String methodName = joinPoint.getSignature().getName();
        final Timer.Sample sample = Timer.start(PrometheusMetricRegistry.getInstance().getRegistry());
        try {
            final Object result = joinPoint.proceed();
            return result;
        } finally {
            sample.stop(Timer.builder(methodName + ".task.dao.execution.time")
                    .description("Time taken to execute DAO method " + methodName)
                    .tag("method", "task." + methodName)
                    .register(PrometheusMetricRegistry.getInstance().getRegistry()));
        }
    }

    @Around(value = "execution(* user.infrastructure.dao.UserDao.*(..)) && @annotation(utils.annotations.MeasurePerformance)")
    public Object measurePersistenceUserExecutionTime(final ProceedingJoinPoint joinPoint) throws Throwable {
        final String methodName = joinPoint.getSignature().getName();
        final Timer.Sample sample = Timer.start(PrometheusMetricRegistry.getInstance().getRegistry());
        try {
            final Object result = joinPoint.proceed();
            return result;
        } finally {
            sample.stop(Timer.builder(methodName + ".user.dao.execution.time")
                    .description("Time taken to execute DAO method " + methodName)
                    .tag("method", "user." + methodName)
                    .register(PrometheusMetricRegistry.getInstance().getRegistry()));
        }
    }
}
