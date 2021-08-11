package edu.emmerson.camel.quarkus.rmq.util;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;

@ApplicationScoped
@Named(ConfigReader.BEAN_NAME)
public class ConfigReader {

    public static final String BEAN_NAME = "configReader";

    public static final String RABBIT_HOST = "RABBIT_HOST";
    public static final String RABBIT_PORT = "RABBIT_PORT";
    public static final String CAMEL_IDEMPOTENT_REPOSITORY_SIZE = "CAMEL_IDEMPOTENT_REPOSITORY_SIZE";
    public static final String CAMEL_MAXIMUM_REDELIVERIES = "CAMEL_MAXIMUM_REDELIVERIES";
    public static final String CAMEL_REDELIVERY_DELAY_MS = "CAMEL_REDELIVERY_DELAY_MS";

    private final Logger log;

    public ConfigReader() {
        this(Logger.getLogger(ConfigReader.class));
    }

    public ConfigReader(Logger log) {
        this.log = log;
    }

    public long getValueAsLong(String prop, Exchange exchange, String defaultValue) {
        return Long.parseLong(getValue(prop, exchange, defaultValue));
    }

    public boolean getValueAsBoolean(String prop, Exchange exchange, String defaultValue) {
        return Boolean.parseBoolean(getValue(prop, exchange, defaultValue));
    }

    public String getValue(String prop, Exchange exchange, String defaultValue) {
        String val = null;

        if (exchange != null) {
            val = exchange.getIn().getHeader(prop, String.class);
        }

        if (StringUtils.isEmpty(val)) {
            val = getValueFromRuntimeContext(prop, defaultValue);
        }
        return val;
    }

    public String getValueFromRuntimeContext(String prop, String defaultValue) {
        String key = getSystemNormalized(prop);
        String val = System.getProperty(key);

        if (StringUtils.isEmpty(val)) {
            val = System.getenv(key);
        }

        if (StringUtils.isEmpty(val)) {
            val = defaultValue;
        }

        log.info(String.format("Returning '%s' for %s property.", val, prop));

        return val;
    }

    public String getSystemNormalized(String prop) {
        return prop.replace("-", "_");
    }

    public String getPublisherQueueEndpoint() {
        StringBuilder sbPub = new StringBuilder();
        sbPub.append("rabbitmq:myexchange?")
                .append("connectionFactory=#").append(ProducerConnectionFactoryService.BEAN_NAME)
                .append("&routingKey=main")
                .append("&queue=myqueue")
                .append("&durable=true")
                .append("&autoDelete=false")
                .append("&exclusive=false")
                .append("&exchangePattern=InOnly");
        return getValueFromRuntimeContext("RMQ_PRODUCER_QUEUE_CS", sbPub.toString());
    }

    public String getConsumerQueueEndpoint() {
        StringBuilder sbConsumer = new StringBuilder();
        sbConsumer.append("rabbitmq:myexchange?").append("connectionFactory=#")
                .append(ProducerConnectionFactoryService.BEAN_NAME)
                .append("&queue=myqueue").append("&routingKey=main").append("&durable=true")
                .append("&autoDelete=false").append("&automaticRecoveryEnabled=true").append("&exclusive=false")
                .append("&autoAck=false").append("&concurrentConsumers=2").append("&prefetchCount=2")
                .append("&prefetchEnabled=true").append("&transferException=true");

        return getValueFromRuntimeContext("RMQ_CONSUMER_QUEUE_CS", sbConsumer.toString());
    }

    public String getConsumerDLQEndpoint() {
        StringBuilder sbConsumer = new StringBuilder();
        sbConsumer.append("rabbitmq:myexchange?").append("connectionFactory=#")
                .append(ProducerConnectionFactoryService.BEAN_NAME)
                .append("&queue=myqueueDLQ").append("&durable=true").append("&autoDelete=false")
                .append("&automaticRecoveryEnabled=true").append("&exchangePattern=InOnly")
                .append("&routingKey=dlq").append("&exclusive=false").append("&autoAck=false")
                .append("&transferException=true");

        return getValueFromRuntimeContext("RMQ_CONSUMER_DLQ_CS", sbConsumer.toString());
    }

    public long getProcessTimeSimulationMs() {
        String pts = getValueFromRuntimeContext("PROCESS_TIME_SIMULATION_MS", "100");
        return Long.parseLong(pts);
    }

    public int getRabbitPort() {
        String tmp = getValueFromRuntimeContext(RABBIT_PORT, "5672");
        return Integer.parseInt(tmp);
    }

    public String getRabbitVirtualHost() {
        return getValueFromRuntimeContext("RABBIT_VIRTUALHOST", "/");
    }

    public String getRabbitPassword() {
        return getValueFromRuntimeContext("RABBIT_PWD", "guest");
    }

    public String getRabbitUsername() {
        return getValueFromRuntimeContext("RABBIT_USER", "guest");
    }

    public String getRabbitHost() {
        return getValueFromRuntimeContext(RABBIT_HOST, "localhost");
    }

    public long getDeliveryThrottle() {
        String pts = getValueFromRuntimeContext("DELIVERY_THROTTLE", "2"); //by default 2 messages
        return Long.parseLong(pts);
    }

    public int getCamelRedeliveryDelay() {
        String tmp = getValueFromRuntimeContext(CAMEL_REDELIVERY_DELAY_MS, "1000");
        return Integer.parseInt(tmp);
    }

    public int getCamelMaximumRedeliveries() {
        String tmp = getValueFromRuntimeContext(CAMEL_MAXIMUM_REDELIVERIES, "2");
        return Integer.parseInt(tmp);
    }

    public int getCamelIdempotentRepositorySize() {
        String tmp = getValueFromRuntimeContext(CAMEL_IDEMPOTENT_REPOSITORY_SIZE, "200");
        return Integer.parseInt(tmp);
    }

}
