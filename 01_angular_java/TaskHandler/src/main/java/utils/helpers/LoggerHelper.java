package utils.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import utils.enumerations.LoggerNameEnum;

public class LoggerHelper {

    public static final Marker SECURITY = MarkerFactory.getMarker("SECURITY");
    public static final Marker GATEWAY = MarkerFactory.getMarker("GATEWAY");
    public static final Marker CONTROLLER = MarkerFactory.getMarker("CONTROLLER");
    public static final Marker PERSISTENCE = MarkerFactory.getMarker("PERSISTENCE");
    public static final Marker BOARD_CONTROLLER = MarkerFactory.getMarker("BOARD_CONTROLLER");
    public static final Marker TASK_CONTROLLER = MarkerFactory.getMarker("TASK_CONTROLLER");
    public static final Marker USER_CONTROLLER = MarkerFactory.getMarker("USER_CONTROLLER");
    public static final Marker GATEWAY_CONTROLLER = MarkerFactory.getMarker("GATEWAY_CONTROLLER");
    public static final Marker BOARD_PERSISTENCE = MarkerFactory.getMarker("BOARD_PERSISTENCE");
    public static final Marker TASK_PERSISTENCE = MarkerFactory.getMarker("TASK_PERSISTENCE");
    public static final Marker USER_PERSISTENCE = MarkerFactory.getMarker("USER_PERSISTENCE");

    static {
        BOARD_CONTROLLER.add(CONTROLLER);
        TASK_CONTROLLER.add(CONTROLLER);
        USER_CONTROLLER.add(CONTROLLER);
        GATEWAY_CONTROLLER.add(CONTROLLER);
        BOARD_PERSISTENCE.add(PERSISTENCE);
        TASK_PERSISTENCE.add(PERSISTENCE);
        USER_PERSISTENCE.add(PERSISTENCE);
    }

    public static Logger getLogger(final LoggerNameEnum loggerNameEnum){
        return LoggerFactory.getLogger(loggerNameEnum.name());
    }

    static public void logError(final Logger LOGGER, final Marker marker, final Throwable exception){
        LOGGER.error(marker, exception.getMessage(), exception);
    }

    static public void logDebug(final Logger LOGGER, final Marker marker, final Exception exception){
        LOGGER.debug(marker, exception.getMessage(), exception);
    }

    static public void logInfo(final Logger LOGGER, final Marker marker, final String message){
        LOGGER.info(marker, message);
    }

    static public void logInfo(final Logger LOGGER, final String message){
        LOGGER.info(message);
    }
}
