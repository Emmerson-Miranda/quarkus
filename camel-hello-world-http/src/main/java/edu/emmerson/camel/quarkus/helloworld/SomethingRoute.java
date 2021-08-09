package edu.emmerson.camel.quarkus.helloworld;

import edu.emmerson.camel.quarkus.helloworld.processor.InjectedBehaviourProcessor;
import org.apache.camel.builder.RouteBuilder;

public class SomethingRoute extends RouteBuilder {

    public static final String ROUTE_ID = "processDoSomething";
    public static final String FROM = "direct:something-route";

    @Override
    public void configure() throws Exception {
        from(FROM)
                .routeId(ROUTE_ID)
                .process(InjectedBehaviourProcessor.BEAN_NAME)
                .log("Doing something with ${header.x-correlation-id}") //show up in opentracing logs
                .setBody(constant("Hello World - doSomething"));

    }

}
