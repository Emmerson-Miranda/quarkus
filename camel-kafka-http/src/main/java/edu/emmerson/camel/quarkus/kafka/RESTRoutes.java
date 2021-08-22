package edu.emmerson.camel.quarkus.kafka;

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

        rest("/publish")
                .post()
                .route()
                .routeId(PublisherRoute.FROM + "-rest")
                .process(new TagProcessor(TRACE_TAG_CORRELATION_ID_NAME, simple("${header.x-correlation-id}")))
                .log("Calling kafka publisher ")
                .to(PublisherRoute.FROM)
                .endRest();

    }
}
