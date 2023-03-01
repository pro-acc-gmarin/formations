package gateway.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import java.net.URISyntaxException;

public class Log4j2Configuration {

    public static void setConfigLocation(){
        LoggerContext context = (LoggerContext) LogManager.getContext(false);

        try {
            context.setConfigLocation(Log4j2Configuration.class.getResource("/configuration/log4j2.xml").toURI());
        } catch (URISyntaxException e) {

            throw new RuntimeException(e);
        }
    }
}
