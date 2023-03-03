package utils.metric;

import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;

public class PrometheusMetricRegistry{
    private static final PrometheusMetricRegistry INSTANCE = new PrometheusMetricRegistry();
    private final PrometheusMeterRegistry registry;

    private PrometheusMetricRegistry() {
        registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    }

    public static PrometheusMetricRegistry getInstance() {
        return INSTANCE;
    }

    public PrometheusMeterRegistry getRegistry() {
        return registry;
    }

    public CollectorRegistry getPrometheusRegistry() {
        return registry.getPrometheusRegistry();
    }
}
