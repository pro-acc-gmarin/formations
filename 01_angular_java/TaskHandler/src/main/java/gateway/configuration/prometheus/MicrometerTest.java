package gateway.configuration.prometheus;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tags;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.junit.jupiter.api.Test;
import utils.metric.PrometheusMetricRegistry;

import static org.junit.jupiter.api.Assertions.*;

class MicrometerTest {
    private final PrometheusMeterRegistry registry = PrometheusMetricRegistry.getInstance().getRegistry();

    @Test
    public void test_metrics(){
        Counter.builder("http_requests_total").description("Http Request Total").tags("method", "GET", "handler",
                "/board").register(registry).increment();
        Counter.builder("http_requests_total").description("Http Request Total").tags("method", "GET", "handler",
                "/user").register(registry).increment();
        Counter.builder("http_requests_total").description("Http Request Total").tags("method", "GET", "handler",
                "/task").register(registry).increment();

        DistributionSummary.builder("http_response_time_milliseconds").description("Request completed time in milliseconds")
                .tags("method", "GET", "handler", "/user")
                .publishPercentiles(.5,.95,.99)
                .register(registry).record(40d);
        DistributionSummary.builder("http_response_time_milliseconds").description("Request completed time in milliseconds")
                .tags("method", "GET", "handler", "/board")
                .publishPercentiles(.5,.95,.99)
                .register(registry).record(50d);

        registry.counter("http_requests_total2", "method", "GET", "status", "200").increment();
        registry.counter("http_requests_total2", "method", "Post", "status", "200").increment();
        registry.counter("http_requests_total2", "method", "GET", "status", "200").increment();

        registry.newCounter(new Meter.Id("query time", Tags.of("select query", "country"), null, "query desc", Meter.Type.COUNTER));
        System.out.println(registry.scrape());
    }

}