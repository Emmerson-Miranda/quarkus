package edu.emmerson.camel.quarkus.helloworld.util;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.Exchange;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ConfigReader {

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
        String val = exchange.getIn().getHeader(prop, String.class);

        if (val == null || "".equals(val)) {
            val = getValueFromRuntimeContext(prop, exchange, defaultValue);
        }
        return val;
    }

    public String getValueFromRuntimeContext(String prop, Exchange exchange, String defaultValue) {
        String key = getSystemNormalized(prop);
        String val = System.getProperty(key);

        if (val == null || "".equals(val)) {
            val = System.getenv(key);
        }

        if (val == null || "".equals(val)) {
            log.info("Returning default value for " + prop);
            val = defaultValue;
        }
        return val;
    }

    public String getSystemNormalized(String prop) {
        return prop.replace("-", "_");
    }

}
