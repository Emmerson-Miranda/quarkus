package edu.emmerson.camel.quarkus.kafka;

import org.apache.camel.builder.RouteBuilder;

public class PublisherRoute extends RouteBuilder {

    public static final String ROUTE_ID = PublisherRoute.class.getName();
    public static final String FROM = "direct:publish-route";

    @Override
    public void configure() throws Exception {
        from(FROM)
                .routeId(ROUTE_ID)
                .log("Message to publish ${header.x-correlation-id}") //show up in opentracing logs
                .to("kafka:{{kafka.topic}}?brokers={{kafka.brokers}}");
    }

}
