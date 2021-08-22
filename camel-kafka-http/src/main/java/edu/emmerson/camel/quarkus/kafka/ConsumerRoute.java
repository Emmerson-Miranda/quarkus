package edu.emmerson.camel.quarkus.kafka;

import org.apache.camel.builder.RouteBuilder;

public class ConsumerRoute extends RouteBuilder {

    public static final String ROUTE_ID = ConsumerRoute.class.getName();
    public static final String FROM = "kafka:{{kafka.topic}}?brokers={{kafka.brokers}}";

    @Override
    public void configure() throws Exception {
        from(FROM)
                .routeId(ROUTE_ID)
                .log("Consumed Headers : ${headers}")
                .log("Consumed Body : ${body}");
    }

}
