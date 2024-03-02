package utils.aspect;

import board.utils.LogsHelper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import utils.helpers.LoggerHelper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@Aspect
public class LoggingAspect {

    @AfterThrowing(pointcut = "execution(* board.application.controller.BoardController.dispatchAction(..)) && @annotation(utils.annotations.HandleException)", throwing = "exception")
    public void logBoardException(final JoinPoint joinPoint, final Throwable exception) {
        final HttpServletResponse response = (HttpServletResponse) joinPoint.getArgs()[0];
        if (exception instanceof IOException || exception instanceof NoSuchMethodException) {
            LogsHelper.error(LoggerHelper.BOARD_CONTROLLER, exception);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else if (exception instanceof SQLException) {
            LogsHelper.error(LoggerHelper.BOARD_PERSISTENCE, exception);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @AfterThrowing(pointcut = "execution(* user.application.controller.UserController.dispatchAction(..)) && @annotation(utils.annotations.HandleException)", throwing = "exception")
    public void logUserException(final JoinPoint joinPoint, final Throwable exception) {
        final HttpServletResponse response = (HttpServletResponse) joinPoint.getArgs()[0];
        if (exception instanceof IOException || exception instanceof NoSuchMethodException) {
            LogsHelper.error(LoggerHelper.USER_CONTROLLER, exception);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else if (exception instanceof SQLException) {
            LogsHelper.error(LoggerHelper.USER_PERSISTENCE, exception);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @AfterThrowing(pointcut = "execution(* task.application.controller.TaskController.dispatchAction(..)) && @annotation(utils.annotations.HandleException)", throwing = "exception")
    public void logTaskException(final JoinPoint joinPoint, final Throwable exception) {
        final HttpServletResponse response = (HttpServletResponse) joinPoint.getArgs()[0];
        if (exception instanceof IOException || exception instanceof NoSuchMethodException) {
            LogsHelper.error(LoggerHelper.TASK_CONTROLLER, exception);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } else if (exception instanceof SQLException) {
            LogsHelper.error(LoggerHelper.TASK_PERSISTENCE, exception);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
