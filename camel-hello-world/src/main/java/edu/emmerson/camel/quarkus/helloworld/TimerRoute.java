package edu.emmerson.camel.quarkus.helloworld;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * A {@link RouteBuilder} demonstrating the use of CDI (Contexts and Dependency Injection).
 * <p>
 * Note that for the {@code @Inject} and {@code @ConfigProperty} annotations to work, this class has to be annotated
 * with {@code @ApplicationScoped}.
 * 
 * Source: https://github.com/apache/camel-quarkus-examples
 */
@ApplicationScoped
public class TimerRoute extends RouteBuilder {

    /**
     * {@code timer.period} is defined in {@code src/main/resources/application.properties}
     */
    @ConfigProperty(name = "timer.period", defaultValue = "1000")
    String period;

    /**
     * An injected bean
     */
    @Inject
    Counter counter;

    @Override
    public void configure() throws Exception {
        fromF("timer:foo?period=%s", period)
                .setBody(exchange -> "Hello world counter : " + counter.increment())
                // the configuration of the log component is done programmatically using CDI
                // by the edu.emmerson.camel.quarkus.helloworld.Beans::log method.
                .to("log:example");
    }
}
