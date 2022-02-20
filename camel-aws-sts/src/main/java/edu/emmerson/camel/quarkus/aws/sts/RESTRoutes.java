package edu.emmerson.camel.quarkus.aws.sts;

import edu.emmerson.poc.aws.assume.library.credentials.CredentialsDelegate;
import edu.emmerson.poc.aws.assume.library.credentials.CredentialsStrategy;
import edu.emmerson.poc.aws.assume.library.pojo.CredentialsRequest;
import edu.emmerson.poc.aws.assume.library.pojo.TemporalCredential;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

//import org.apache.camel.opentracing.TagProcessor;

/**
 * This example is based on https://camel.apache.org/camel-quarkus/latest/user-guide/examples.html
 * 
 * @author emmersonmiranda
 *
 */
@RegisterForReflection(targets = { CredentialsRequest.class, TemporalCredential.class })
public class RESTRoutes extends RouteBuilder {

    private static final String TRACE_TAG_CORRELATION_ID_NAME = "myCorrelationId";

    final CredentialsStrategy cs;
    final CredentialsDelegate cd;

    public RESTRoutes() {
        cd = new CredentialsDelegate();
        cs = cd.getCredentialsStratey();
    }

    @Override
    public void configure() throws Exception {

        restConfiguration().bindingMode(RestBindingMode.json);

        rest("/hello")
                .get()
                .route()
                .routeId("helo")
                //.process(new TagProcessor(TRACE_TAG_CORRELATION_ID_NAME, simple("${header.x-correlation-id}")))
                .setBody(constant("Hello world"))
                .log("Calling hello route ")
                .endRest();

        rest("/credentials")
                .post()
                .type(CredentialsRequest.class).outType(TemporalCredential.class)
                .consumes("application/json").produces("application/json")
                .route()
                .routeId("getCredentials")
                //.process(new TagProcessor(TRACE_TAG_CORRELATION_ID_NAME, simple("${header.x-correlation-id}")))
                .log("Getting AWS Temporal Credentials")
                .removeHeader(Exchange.HTTP_PATH)
                .process(new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {
                        CredentialsRequest body = exchange.getIn().getBody(CredentialsRequest.class);
                        System.out.println("Request for temporal credentials: " + body);
                        TemporalCredential cred = cd.loadCredentialsStrategy(cs, body);
                        exchange.getIn().setBody(cred);
                    }

                })
                .endRest();

    }
}
