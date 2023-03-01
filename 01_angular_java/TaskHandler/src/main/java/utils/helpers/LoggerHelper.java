package utils.helpers;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class LoggerHelper {

    public static final Marker SECURITY = MarkerManager.getMarker("SECURITY");
    public static final Marker GATEWAY = MarkerManager.getMarker("GATEWAY");
    public static final Marker CONTROLLER = MarkerManager.getMarker("CONTROLLER");
    public static final Marker PERSISTENCE = MarkerManager.getMarker("PERSISTENCE");
    public static final Marker BOARD_CONTROLLER = MarkerManager.getMarker("BOARD_CONTROLLER");
    public static final Marker TASK_CONTROLLER = MarkerManager.getMarker("TASK_CONTROLLER");
    public static final Marker USER_CONTROLLER = MarkerManager.getMarker("USER_CONTROLLER");
    public static final Marker GATEWAY_CONTROLLER = MarkerManager.getMarker("GATEWAY_CONTROLLER");
    public static final Marker BOARD_PERSISTENCE = MarkerManager.getMarker("BOARD_PERSISTENCE");
    public static final Marker TASK_PERSISTENCE = MarkerManager.getMarker("TASK_PERSISTENCE");
    public static final Marker USER_PERSISTENCE = MarkerManager.getMarker("USER_PERSISTENCE");

    static {
        BOARD_CONTROLLER.addParents(CONTROLLER);
        TASK_CONTROLLER.addParents(CONTROLLER);
        USER_CONTROLLER.addParents(CONTROLLER);
        GATEWAY_CONTROLLER.addParents(CONTROLLER);
        BOARD_PERSISTENCE.addParents(PERSISTENCE);
        TASK_PERSISTENCE.addParents(PERSISTENCE);
        USER_PERSISTENCE.addParents(PERSISTENCE);
    }

    static public void logError(Logger LOGGER, Marker marker, Exception exception){
        LOGGER.atError().withThrowable(exception).withMarker(marker).log(exception.getMessage());
    }

    static public void logDebug(Logger LOGGER, Marker marker, Exception exception){
        LOGGER.atDebug().withThrowable(exception).withMarker(marker).log(exception.getMessage());
    }

    static public void logInfo(Logger LOGGER, Marker marker, String message){
        LOGGER.atInfo().withMarker(marker).log(message);
    }

    static public void logTrace(Logger LOGGER, Marker marker, Exception exception){
        LOGGER.atTrace().withThrowable(exception).withMarker(marker).log(exception.getMessage());
    }
}
