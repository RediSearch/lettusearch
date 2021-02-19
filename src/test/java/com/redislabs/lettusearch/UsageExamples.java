package com.redislabs.lettusearch;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.support.ConnectionPoolSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class UsageExamples extends AbstractBaseTest {

    private static class Map {

        public static java.util.Map<String, String> of(String key, String value) {
            java.util.Map<String, String> map = new HashMap<>();
            map.put(key, value);
            return map;
        }

    }

    @Test
    public void basic() {
        sync.flushall();
        // tag::basic[]
        RediSearchClient client = RediSearchClient.create(RedisURI.create(host, port)); // <1>
        StatefulRediSearchConnection<String, String> connection = client.connect(); // <2>
        RediSearchCommands<String, String> commands = connection.sync(); // <3>
        commands.create("beers", Field.text("name").build()); // <4>
        commands.hmset("beer:1", Map.of("name", "Chouffe")); // <5>
        SearchResults<String, String> results = commands.search("beers", "chou*"); // <6>
        System.out.println("Found " + results.getCount() + " documents matching query");
        for (Document<String, String> doc : results) {
            System.out.println(doc);
        }
        // end::basic[]
        Assertions.assertEquals(1, results.size());
        Assertions.assertEquals("Chouffe", results.get(0).get("name"));
        Assertions.assertEquals("beer:1", results.get(0).getId());
    }

    @Test
    public void pipelining() {
        sync.flushall();
        java.util.Map<String, String> doc1 = mapOf("id", "doc1", NAME, "somevalue");
        java.util.Map<String, String> doc2 = mapOf("id", "doc2", NAME, "somevalue");
        List<java.util.Map<String, String>> docs = Arrays.asList(doc1, doc2);
        connection.sync().create("idx", Field.text(NAME).build());
        RediSearchClient client = RediSearchClient.create(RedisURI.create(host, port));
        StatefulRediSearchConnection<String, String> connection = client.connect();
        // tag::pipelining[]
        RediSearchAsyncCommands<String, String> commands = connection.async(); // <1>
        commands.setAutoFlushCommands(false); // <2>
        List<RedisFuture<?>> futures = new ArrayList<>();
        for (java.util.Map<String, String> doc : docs) {
            RedisFuture<String> future = commands.hmset(doc.get("id"), doc);// <3>
            futures.add(future); // <4>
        }
        commands.flushCommands(); // <5>
        for (RedisFuture<?> future : futures) {
            try {
                future.get(1, TimeUnit.SECONDS); // <6>
            } catch (InterruptedException e) {
                log.debug("Command interrupted", e);
            } catch (ExecutionException e) {
                log.error("Command execution returned an error", e);
            } catch (TimeoutException e) {
                log.error("Command timed out", e);
            }
        }
        commands.setAutoFlushCommands(true); // <7>
        // end::pipelining[]

        Assertions.assertEquals(docs.size(), connection.sync().search("idx", "*").size());
    }

    @Test
    public void connectionPooling() throws Exception {
        sync.flushall();
        String index = "idx";
        connection.sync().create(index, Field.text(NAME).build());
        RediSearchClient client = RediSearchClient.create(RedisURI.create(host, port));
        // tag::connectionPooling[]
        GenericObjectPoolConfig<StatefulRediSearchConnection<String, String>> config = new GenericObjectPoolConfig<>(); // <1>
        config.setMaxTotal(8);
        // config.setX...
        GenericObjectPool<StatefulRediSearchConnection<String, String>> pool = ConnectionPoolSupport.createGenericObjectPool(client::connect, config); // <2>
        try (StatefulRediSearchConnection<String, String> connection = pool.borrowObject()) { // <3>
            RediSearchAsyncCommands<String, String> commands = connection.async(); // <4>
            // ...
        }
        // end::connectionPooling[]
        pool.close();
    }
}
