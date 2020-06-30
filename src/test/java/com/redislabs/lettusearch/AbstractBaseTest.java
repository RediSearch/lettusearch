package com.redislabs.lettusearch;

import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.suggest.Suggestion;
import io.lettuce.core.RedisURI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.List;

import static com.redislabs.lettusearch.Beers.*;

@Testcontainers
public abstract class AbstractBaseTest {

    protected final static String SUGINDEX = "beersSug";

    private RediSearchClient client;
    protected StatefulRediSearchConnection<String, String> connection;
    protected List<Document<String, String>> beers;
    protected RediSearchCommands<String, String> commands;

    @Container
    private final GenericContainer redis = new GenericContainer("redislabs/redisearch:latest").withExposedPorts(6379);

    @BeforeEach
    public void setup() throws IOException {
        beers = load();
        client = RediSearchClient.create(RedisURI.create(redis.getHost(), redis.getFirstMappedPort()));
        connection = client.connect();
        commands = connection.sync();
        commands.flushall();
        commands.create(INDEX, SCHEMA, null);
        for (Document<String, String> beer : beers) {
            commands.add(INDEX, beer, null);
            commands.sugadd(SUGINDEX, Suggestion.<String>builder().string(beer.get(NAME)).score(1d).build(), false);
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
