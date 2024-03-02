package user.utils;

import org.slf4j.Logger;
import org.slf4j.Marker;
import utils.enumerations.LoggerNameEnum;
import utils.helpers.LoggerHelper;

public class LogsHelper {
    private static final Logger LOGGER = LoggerHelper.getLogger(LoggerNameEnum.USER);


    public static void info(final Marker marker, final String message){
        LoggerHelper.logInfo(LOGGER, marker, message);
    }
    public static void error( Marker marker, Throwable exception){
        LoggerHelper.logError(LOGGER, marker, exception);
    }
}
