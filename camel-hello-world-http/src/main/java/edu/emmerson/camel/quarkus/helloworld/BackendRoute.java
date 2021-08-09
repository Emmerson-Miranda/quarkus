package edu.emmerson.camel.quarkus.helloworld;

import edu.emmerson.camel.quarkus.helloworld.processor.InjectedBehaviourProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

public class BackendRoute extends RouteBuilder {

    public static final String ROUTE_ID = "backend";
    public static final String FROM = "direct:backend-route";

    @Override
    public void configure() throws Exception {
        from(FROM)
                .routeId(ROUTE_ID)
                .process(InjectedBehaviourProcessor.BEAN_NAME)
                .log("calling backend with id ${header.x-correlation-id}") //show up in opentracing logs
                .removeHeader(Exchange.HTTP_PATH)
                .toD("${header.CamelHttpUri}");

    }

}
