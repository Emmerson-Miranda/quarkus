package edu.emmerson.camel.quarkus.helloworld;

import org.apache.camel.builder.RouteBuilder;

public class ByeRoute extends RouteBuilder {

    public static final String ROUTE_ID = "processGoodbye";
    public static final String FROM = "direct:bye-route";

    @Override
    public void configure() throws Exception {
        from(FROM)
                .routeId(ROUTE_ID)
                .delay(simple("${random(1000, 2000)}"))
                .log("Bye ${header.x-correlation-id}") //show up in opentracing logs
                .setBody(constant("Hello World - goodbye!"));

    }

}
