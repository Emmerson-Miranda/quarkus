package edu.emmerson.camel.quarkus.rmq.route;

import edu.emmerson.camel.quarkus.rmq.processor.InjectedBehaviourProcessor;
import edu.emmerson.camel.quarkus.rmq.util.ControlBusPojo;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.camel.builder.RouteBuilder;

/**
 * https://camel.apache.org/components/3.11.x/controlbus-component.html
 * 
 * @author emmersonmiranda
 *
 */
@RegisterForReflection(targets = { ControlBusPojo.class })
public class ControlBusRouteBuilder extends RouteBuilder {

    public static final String ROUTE_ID = ControlBusRouteBuilder.class.getName();

    public static final String FROM = "direct:controlbus-route";

    @Override
    public void configure() throws Exception {

        from(FROM)
                .routeId(ROUTE_ID)
                .process(InjectedBehaviourProcessor.BEAN_NAME)
                .log("Calling Camel Control Bus component with: ${body}")
                .toD("${body}");
    }

}
