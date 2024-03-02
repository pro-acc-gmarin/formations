package gateway.configuration.prometheus;

import gateway.utils.LogsHelper;
import io.micrometer.core.instrument.binder.jvm.DiskSpaceMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import utils.helpers.LoggerHelper;
import utils.metric.PrometheusMetricRegistry;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

public class PrometheusServletContext implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        new JvmThreadMetrics().bindTo(PrometheusMetricRegistry.getInstance().getRegistry());
        new JvmGcMetrics().bindTo(PrometheusMetricRegistry.getInstance().getRegistry());
        new JvmMemoryMetrics().bindTo(PrometheusMetricRegistry.getInstance().getRegistry());
        new DiskSpaceMetrics(new File("/")).bindTo(PrometheusMetricRegistry.getInstance().getRegistry());
        new ProcessorMetrics().bindTo(PrometheusMetricRegistry.getInstance().getRegistry());
        new UptimeMetrics().bindTo(PrometheusMetricRegistry.getInstance().getRegistry());
        LogsHelper.info(LoggerHelper.GATEWAY, "Prometheus registry initialized.");
    }

}
