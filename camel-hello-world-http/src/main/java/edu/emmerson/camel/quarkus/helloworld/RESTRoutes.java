package edu.emmerson.camel.quarkus.helloworld;

import edu.emmerson.camel.quarkus.helloworld.processor.InjectedBehaviourProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.opentracing.TagProcessor;

/**
 * This example is based on https://camel.apache.org/camel-quarkus/latest/user-guide/examples.html
 * 
 * @author emmersonmiranda
 *
 */
public class RESTRoutes extends RouteBuilder {

    private static final String TRACE_TAG_CORRELATION_ID_NAME = "myCorrelationId";

    @Override
    public void configure() throws Exception {

        restConfiguration().bindingMode(RestBindingMode.json);

        rest("/greeting")
                .get()
                .route()
                .routeId("processGreeting2GET")
                .process(new TagProcessor(TRACE_TAG_CORRELATION_ID_NAME, simple("${header.x-correlation-id}")))
                .log("Calling welcome route ")
                .to(WelcomeRoute.FROM)
                .log("Calling bye route ")
                .to(ByeRoute.FROM)
                .endRest()

                .post()
                .route()
                .routeId("processGreeting2POST")
                .process(new TagProcessor(TRACE_TAG_CORRELATION_ID_NAME, simple("${header.x-correlation-id}")))
                .log("Calling welcome route ")
                .to(WelcomeRoute.FROM)
                .log("Calling something route ")
                .to(SomethingRoute.FROM)
                .log("Calling bye route ")
                .to(ByeRoute.FROM)
                .endRest();

        rest("/getgoogle")
                .get()
                .route()
                .routeId("getGoogle")
                .process(new TagProcessor(TRACE_TAG_CORRELATION_ID_NAME, simple("${header.x-correlation-id}")))
                .process(InjectedBehaviourProcessor.BEAN_NAME)
                .log("Getting google home page")
                .removeHeader(Exchange.HTTP_PATH)
                .to("http://www.google.com?bridgeEndpoint=true")
                .endRest();

        rest("/callbackend")
                .get()
                .route()
                .routeId("callBackend")
                .process(new TagProcessor(TRACE_TAG_CORRELATION_ID_NAME, simple("${header.x-correlation-id}")))
                .process(InjectedBehaviourProcessor.BEAN_NAME)
                .log("Calling backend")
                .to(BackendRoute.FROM)
                .endRest();

    }
}
