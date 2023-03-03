package utils.aspect;

import board.application.controller.BoardController;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Timer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import utils.helpers.LoggerHelper;
import utils.metric.PrometheusMetricRegistry;

import java.time.Duration;

@Aspect
public class PerformanceAspect {

    private static final Logger LOGGER = LogManager.getLogger(PerformanceAspect.class);

    @Around(value = "execution(* board.infrastructure.dao.BoardDao.*(..)) && @annotation(utils.annotations.MeasurePerformance)")
    public Object measurePersistenceBoardExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Timer.Sample sample = Timer.start(PrometheusMetricRegistry.getInstance().getRegistry());
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
    public Object measurePersistenceTaskExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Timer.Sample sample = Timer.start(PrometheusMetricRegistry.getInstance().getRegistry());
        try {
            Object result = joinPoint.proceed();
            return result;
        } finally {
            sample.stop(Timer.builder(methodName + ".task.dao.execution.time")
                    .description("Time taken to execute DAO method " + methodName)
                    .tag("method", "task." + methodName)
                    .register(PrometheusMetricRegistry.getInstance().getRegistry()));
        }
    }

    @Around(value = "execution(* user.infrastructure.dao.UserDao.*(..)) && @annotation(utils.annotations.MeasurePerformance)")
    public Object measurePersistenceUserExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Timer.Sample sample = Timer.start(PrometheusMetricRegistry.getInstance().getRegistry());
        try {
            Object result = joinPoint.proceed();
            return result;
        } finally {
            sample.stop(Timer.builder(methodName + ".user.dao.execution.time")
                    .description("Time taken to execute DAO method " + methodName)
                    .tag("method", "user." + methodName)
                    .register(PrometheusMetricRegistry.getInstance().getRegistry()));
        }
    }
}
