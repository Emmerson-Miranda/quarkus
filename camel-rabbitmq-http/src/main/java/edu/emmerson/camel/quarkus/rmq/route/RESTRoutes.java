package edu.emmerson.camel.quarkus.rmq.route;

import edu.emmerson.camel.quarkus.rmq.util.ControlBusPojo;
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

    private static final String TRACE_TAG_CORRELATION_ID_NAME = "correlationId";

    @Override
    public void configure() throws Exception {

        restConfiguration().bindingMode(RestBindingMode.off);

        rest("/publish")
                .post()
                .route()
                .routeId(ProducerRouteBuilder.ROUTE_ID + "-rest")
                .log("HTTP Landing message: ${body}")
                .process(new TagProcessor(TRACE_TAG_CORRELATION_ID_NAME, simple("${header.x-correlation-id}")))
                .to(ProducerRouteBuilder.FROM)
                .endRest();

        rest("/controlbus").bindingMode(RestBindingMode.auto)
                .post().type(ControlBusPojo.class)
                .route()
                .routeId(ControlBusRouteBuilder.ROUTE_ID + "-rest")
                .log("Command: ${body}")
                .to(ControlBusRouteBuilder.FROM)
                .endRest();

    }
}
