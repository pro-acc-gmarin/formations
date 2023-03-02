package utils.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import utils.helpers.LoggerHelper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@Aspect
public class LoggingAspect {

    Logger LOGGER = LogManager.getLogger(LoggingAspect.class);

    @AfterThrowing(pointcut = "execution(* board.application.controller.BoardController.dispatchAction(..)) && @annotation(utils.annotations.HandleException)", throwing = "exception")
    public void logBoardException(JoinPoint joinPoint, Throwable exception) {
        HttpServletResponse response = (HttpServletResponse) joinPoint.getArgs()[0];
        if (exception instanceof IOException || exception instanceof NoSuchMethodException) {
            LOGGER.atError().withMarker(LoggerHelper.BOARD_CONTROLLER).withThrowable(exception).log(exception.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else if (exception instanceof SQLException) {
            LOGGER.atError().withMarker(LoggerHelper.BOARD_PERSISTENCE).withThrowable(exception).log(exception.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @AfterThrowing(pointcut = "execution(* user.application.controller.UserController.dispatchAction(..)) && @annotation(utils.annotations.HandleException)", throwing = "exception")
    public void logUserException(JoinPoint joinPoint, Throwable exception) {
        HttpServletResponse response = (HttpServletResponse) joinPoint.getArgs()[0];
        if (exception instanceof IOException || exception instanceof NoSuchMethodException) {
            LOGGER.atError().withMarker(LoggerHelper.USER_CONTROLLER).withThrowable(exception).log(exception.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else if (exception instanceof SQLException) {
            LOGGER.atError().withMarker(LoggerHelper.USER_CONTROLLER).withThrowable(exception).log(exception.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @AfterThrowing(pointcut = "execution(* task.application.controller.TaskController.dispatchAction(..)) && @annotation(utils.annotations.HandleException)", throwing = "exception")
    public void logTaskException(JoinPoint joinPoint, Throwable exception) {
        HttpServletResponse response = (HttpServletResponse) joinPoint.getArgs()[0];
        if (exception instanceof IOException || exception instanceof NoSuchMethodException) {
            LOGGER.atError().withMarker(LoggerHelper.TASK_CONTROLLER).withThrowable(exception).log(exception.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else if (exception instanceof SQLException) {
            LOGGER.atError().withMarker(LoggerHelper.TASK_PERSISTENCE).withThrowable(exception).log(exception.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
