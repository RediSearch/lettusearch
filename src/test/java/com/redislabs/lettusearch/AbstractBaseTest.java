package com.redislabs.lettusearch;

import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.suggest.Suggestion;
import io.lettuce.core.RedisURI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.List;

@Testcontainers
public abstract class AbstractBaseTest {

    protected final static String SUGINDEX = "beersSug";

    private RediSearchClient client;
    protected StatefulRediSearchConnection<String, String> connection;
    protected static List<Document<String, String>> beers;
    protected RediSearchCommands<String, String> sync;
    protected RediSearchAsyncCommands<String, String> async;
    protected RediSearchReactiveCommands<String, String> reactive;

    @Container
    @SuppressWarnings("rawtypes")
    private static final GenericContainer redisearch = new GenericContainer("redislabs/redisearch:1.99.3").withExposedPorts(6379);

    @BeforeAll
    public static void load() throws IOException {
        beers = Beers.load();
    }

    @BeforeEach
    public void setup() {
        client = RediSearchClient.create(RedisURI.create(redisearch.getHost(), redisearch.getFirstMappedPort()));
        connection = client.connect();
        sync = connection.sync();
        async = connection.async();
        reactive = connection.reactive();
        sync.flushall();
        sync.create(Beers.INDEX, Beers.SCHEMA);
        for (Document<String, String> beer : beers) {
            sync.add(Beers.INDEX, beer);
            sync.sugadd(SUGINDEX, Suggestion.<String>builder().string(beer.get(Beers.NAME)).score(1d).build(), false);
        }
    }

    @AfterEach
    public void teardown() {
        if (connection != null) {
            connection.close();
        }
        if (client != null) {
            client.shutdown();
        }
    }

}
