package edu.emmerson.camel.quarkus.helloworld.processor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import edu.emmerson.camel.quarkus.helloworld.util.ConfigReader;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.http.client.methods.HttpGet;
import org.jboss.logging.Logger;

@ApplicationScoped
@Named(InjectedBehaviourProcessor.BEAN_NAME)
public class InjectedBehaviourProcessor implements Processor {

    public static final String BEAN_NAME = "injectedBehaviourProcessor";

    final Logger log;

    @Inject
    private ConfigReader configReader;

    public InjectedBehaviourProcessor() {
        this(Logger.getLogger(InjectedBehaviourProcessor.class));
    }

    public InjectedBehaviourProcessor(Logger log) {
        this.log = log;
    }

    @Override
    public void process(Exchange exchange) throws Exception {

        long delay = configReader.getValueAsLong("x-delay", exchange, "0");
        boolean throwError = configReader.getValueAsBoolean("x-throw-error", exchange, "false");
        String uri = configReader.getValue("x-uri", exchange, "");

        if (delay > 0) {
            log.info("Sleeping " + delay);
            Thread.sleep(delay);
        }

        if (throwError) {
            log.info("Throwing an exception");
            throw new Exception("Throwing an exception as per parameter value");
        }

        if (uri != null && !"".equals(uri)) {
            String method = configReader.getValue("x-method", exchange, HttpGet.METHOD_NAME);

            log.info(String.format("URI : %s, METHOD: %s", uri, method));

            exchange.getIn().setHeader(Exchange.HTTP_URI, uri);
            exchange.getIn().setHeader(Exchange.HTTP_METHOD, method);

        }

    }

}
