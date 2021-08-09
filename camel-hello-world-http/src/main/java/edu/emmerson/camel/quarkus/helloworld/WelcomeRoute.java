package edu.emmerson.camel.quarkus.helloworld;

import edu.emmerson.camel.quarkus.helloworld.processor.InjectedBehaviourProcessor;
import org.apache.camel.builder.RouteBuilder;

public class WelcomeRoute extends RouteBuilder {

    public static final String ROUTE_ID = "processGreeting";
    public static final String FROM = "direct:welcome-route";

    @Override
    public void configure() throws Exception {
        from(FROM)
                .routeId(ROUTE_ID)
                .process(InjectedBehaviourProcessor.BEAN_NAME)
                .log("Welcome ${header.x-correlation-id}") //show up in opentracing logs
                .setBody(constant("Hello World - greetings!"));
    }

}
