#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

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
                .process(new TagProcessor(TRACE_TAG_CORRELATION_ID_NAME, simple("${symbol_dollar}{header.x-correlation-id}")))
                .log("Calling welcome route ")
                .to(WelcomeRoute.FROM)
                .log("Calling bye route ")
                .to(ByeRoute.FROM)
                .endRest()

                .post()
                .route()
                .routeId("processGreeting2POST")
                .process(new TagProcessor(TRACE_TAG_CORRELATION_ID_NAME, simple("${symbol_dollar}{header.x-correlation-id}")))
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
                .process(new TagProcessor(TRACE_TAG_CORRELATION_ID_NAME, simple("${symbol_dollar}{header.x-correlation-id}")))
                .log("Getting google home page")
                .removeHeader(Exchange.HTTP_PATH)
                //.setHeader(Exchange.HTTP_URI, constant("https://www.google.co.uk"))
                //.setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http.HttpMethods.GET))
                .to("http://www.google.com?bridgeEndpoint=true")
                .endRest();

    }
}
