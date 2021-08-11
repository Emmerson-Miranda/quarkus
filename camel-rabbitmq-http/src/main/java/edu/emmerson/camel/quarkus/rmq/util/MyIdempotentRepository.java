package edu.emmerson.camel.quarkus.rmq.util;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.support.LRUCacheFactory;
import org.apache.camel.support.processor.idempotent.MemoryIdempotentRepository;

@ApplicationScoped
public class MyIdempotentRepository extends MemoryIdempotentRepository {

    public MyIdempotentRepository() {
        this(LRUCacheFactory.newLRUCache(1000));

    }

    public MyIdempotentRepository(Map<String, Object> cache) {
        super(cache);
    }

}
