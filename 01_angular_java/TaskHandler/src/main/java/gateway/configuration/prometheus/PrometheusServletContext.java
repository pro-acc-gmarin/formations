package gateway.configuration.prometheus;

import gateway.configuration.DataSourceConfiguration;
import gateway.configuration.Log4j2Configuration;
import io.micrometer.core.instrument.binder.jvm.DiskSpaceMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.prometheus.client.exporter.HTTPServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.helpers.LoggerHelper;
import utils.metric.PrometheusMetricRegistry;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

public class PrometheusServletContext implements ServletContextListener {

    private static final Logger LOGGER = LogManager.getLogger(PrometheusServletContext.class);
    @Override
    public void contextInitialized(ServletContextEvent event) {
        new JvmThreadMetrics().bindTo(PrometheusMetricRegistry.getInstance().getRegistry());
        new JvmGcMetrics().bindTo(PrometheusMetricRegistry.getInstance().getRegistry());
        new JvmMemoryMetrics().bindTo(PrometheusMetricRegistry.getInstance().getRegistry());
        new DiskSpaceMetrics(new File("/")).bindTo(PrometheusMetricRegistry.getInstance().getRegistry());
        new ProcessorMetrics().bindTo(PrometheusMetricRegistry.getInstance().getRegistry());
        new UptimeMetrics().bindTo(PrometheusMetricRegistry.getInstance().getRegistry());
        LoggerHelper.logInfo(LOGGER, LoggerHelper.GATEWAY, "Prometheus registry initialized.");
    }

}
