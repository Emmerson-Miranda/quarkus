package edu.emmerson.camel.quarkus.rmq.route;

import java.time.Instant;
import java.util.UUID;

import javax.inject.Inject;

import edu.emmerson.camel.quarkus.rmq.processor.InjectedBehaviourProcessor;
import edu.emmerson.camel.quarkus.rmq.util.ConfigReader;
import org.apache.camel.builder.RouteBuilder;

public class ProducerRouteBuilder extends RouteBuilder {

    public static final String ROUTE_ID = ProducerRouteBuilder.class.getName();

    public static final String FROM = "direct:publish-message";

    @Inject
    private ConfigReader configReader = new ConfigReader();

    @Override
    public void configure() throws Exception {

        from(FROM)
                .routeId(ROUTE_ID)
                .process(InjectedBehaviourProcessor.BEAN_NAME)
                .process().message(m -> {
                    m.setHeader("custom.messageId", UUID.randomUUID().toString());
                    m.setHeader("custom.currentTimeMillis", System.currentTimeMillis());
                    m.setHeader("custom.Date", Instant.now().toString());
                    m.setHeader("custom.XcorrelationID", m.getHeader("x-correlation-id"));
                })
                .log("Message to be stored in RabbitMQ: ${body}")
                .to(configReader.getPublisherQueueEndpoint());
    }

}
