package gateway.utils;

import org.slf4j.Logger;
import org.slf4j.Marker;
import utils.enumerations.LoggerNameEnum;
import utils.helpers.LoggerHelper;

public class LogsHelper {
    private static final Logger LOGGER = LoggerHelper.getLogger(LoggerNameEnum.GATEWAY);


    public static void info(final Marker marker, final String message){
        LoggerHelper.logInfo(LOGGER, marker, message);
    }

    public static void info(final String message){
        LoggerHelper.logInfo(LOGGER, message);
    }

    public static void error(final Marker marker, final Throwable exception){
        LoggerHelper.logError(LOGGER, marker, exception);
    }
}
