package task.utils;

import org.slf4j.Logger;
import org.slf4j.Marker;
import utils.enumerations.LoggerNameEnum;
import utils.helpers.LoggerHelper;

public class LogsHelper {
    private static final Logger LOGGER = LoggerHelper.getLogger(LoggerNameEnum.TASK);


    public static void info(final Marker marker, final String message){
        LoggerHelper.logInfo(LOGGER, marker, message);
    }
    public static void error( Marker marker, Throwable exception){
        LoggerHelper.logError(LOGGER, marker, exception);
    }
}
