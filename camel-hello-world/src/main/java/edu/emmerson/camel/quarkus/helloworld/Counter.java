package edu.emmerson.camel.quarkus.helloworld;

import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.context.ApplicationScoped;

/**
 * Source: https://github.com/apache/camel-quarkus-examples
 */
@ApplicationScoped
public class Counter {
    private final AtomicInteger value = new AtomicInteger(0);

    public int increment() {
        return value.incrementAndGet();
    }

    public int getValue() {
        return value.get();
    }
}
