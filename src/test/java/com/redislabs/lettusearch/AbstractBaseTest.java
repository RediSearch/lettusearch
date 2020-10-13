package com.redislabs.lettusearch;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.suggest.Suggestion;

import io.lettuce.core.RedisURI;

@Testcontainers
public abstract class AbstractBaseTest {

    protected final static String SUGINDEX = "beersSug";

    private RediSearchClient client;
    protected StatefulRediSearchConnection<String, String> connection;
    protected static List<Document<String, String>> beers;
    protected RediSearchCommands<String, String> sync;
    protected RediSearchAsyncCommands<String, String> async;
    protected RediSearchReactiveCommands<String, String> reactive;

	protected String host;
	protected int port;

    @Container
    @SuppressWarnings("rawtypes")
    public static final GenericContainer REDISEARCH = new GenericContainer(DockerImageName.parse("redislabs/redisearch:latest")).withExposedPorts(6379);

    @BeforeAll
    public static void load() throws IOException {
        beers = Beers.load();
    }

    @BeforeEach
    public void setup() {
    	host = REDISEARCH.getHost();
    	port = REDISEARCH.getFirstMappedPort();
        client = RediSearchClient.create(RedisURI.create(host, port));
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
