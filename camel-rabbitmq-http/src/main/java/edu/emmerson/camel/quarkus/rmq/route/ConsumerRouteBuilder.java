package edu.emmerson.camel.quarkus.rmq.route;

import javax.inject.Inject;

import edu.emmerson.camel.quarkus.rmq.processor.InjectedBehaviourProcessor;
import edu.emmerson.camel.quarkus.rmq.util.ConfigReader;
import edu.emmerson.camel.quarkus.rmq.util.MyIdempotentRepository;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;

import static org.apache.camel.Exchange.HTTP_METHOD;
import static org.apache.camel.component.http.HttpMethods.POST;

/**
 * 
 * @author emmersonmiranda
 * @link   https://camel.apache.org/manual/latest/exception-clause.html
 *
 */
@RegisterForReflection(targets = { IllegalStateException.class, Throwable.class })
public class ConsumerRouteBuilder extends RouteBuilder {

    public static final String ROUTE_ID = ConsumerRouteBuilder.class.getName();
    public static final String FROM = "direct:target";

    public static final String RABBITMQ_ROUTING_KEY = "rabbit.consumer";

    @Inject
    MyIdempotentRepository myIdempotentRepository = new MyIdempotentRepository();

    @Inject
    private ConfigReader configReader = new ConfigReader();

    @Override
    public void configure() throws Exception {

        int maximumRedeliveries = configReader.getCamelMaximumRedeliveries();
        int redeliveryDelay = configReader.getCamelRedeliveryDelay();
        long deliveryThrottle = configReader.getDeliveryThrottle();

        //
        // error handling
        //
        onException(Throwable.class)
                .maximumRedeliveries(maximumRedeliveries)
                .handled(true)
                .log("onException:: ${header.x-correlation-id} :: ${exception.message} :: ${exception}")
                .asyncDelayedRedelivery()
                .redeliveryDelay(redeliveryDelay)

                .setHeader(RabbitMQConstants.ROUTING_KEY, constant("dlq"))
                // sending the message to DLQ
                .toD(configReader.getConsumerDLQEndpoint())
                .log("Message moved to DLQ");

        /*
         * https://camel.apache.org/components/latest/rabbitmq-component.html
         CamelRabbitmqRequeue
          	
        	This is used by the consumer to control rejection of the message.
        	 When the consumer is complete processing the exchange, and if the exchange failed, 
        	 then the consumer is going to reject the message from the RabbitMQ broker. 
        	 The value of this header controls this behavior. 
        	 
        	 If the value is false (by default) then the message is discarded/dead-lettered. 
        	 If the value is true, then the message is re-queued.
         * */

        //
        // consuming messages
        //
        from(configReader.getConsumerQueueEndpoint())
                .routeId(ROUTE_ID + "-rmq-consumer")
                .idempotentConsumer(header("x-correlation-id"), myIdempotentRepository)
                .to(FROM);

        //

        // processing message
        //
        from(FROM)
                .routeId(ROUTE_ID)
                .log("Message to send to upstream: ${header.x-correlation-id}")
                .throttle(deliveryThrottle)
                .log("Delivering: ${body}")
                .process(InjectedBehaviourProcessor.BEAN_NAME)
                .setHeader(HTTP_METHOD, constant(POST))
                .to("http://oldhost")
                .log("Message delivered to upstream: ${header.x-correlation-id}");
        ;
    }

}
