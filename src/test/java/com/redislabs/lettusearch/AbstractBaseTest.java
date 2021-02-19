package com.redislabs.lettusearch;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Testcontainers
public abstract class AbstractBaseTest {

    protected final static int BEER_COUNT = 2348;
    protected final static String SUGINDEX = "beersSug";

    public final static String ABV = "abv";
    public final static String ID = "id";
    public final static String NAME = "name";
    public final static String STYLE = "style";
    public final static Field<String>[] SCHEMA = new Field[]{Field.text(NAME).matcher(Field.Text.PhoneticMatcher.English).build(), Field.tag(STYLE).sortable(true).build(), Field.numeric(ABV).sortable(true).build()};
    public final static String INDEX = "beers";

    private static RediSearchClient client;
    protected static StatefulRediSearchConnection<String, String> connection;
    protected static RediSearchCommands<String, String> sync;
    protected static RediSearchAsyncCommands<String, String> async;
    protected static RediSearchReactiveCommands<String, String> reactive;
    protected static String host;
    protected static int port;

    @Container
    @SuppressWarnings("rawtypes")
    public static final GenericContainer REDISEARCH = new GenericContainer(DockerImageName.parse("redislabs/redisearch:latest")).withExposedPorts(6379);

    @BeforeAll
    public static void setup() {
        host = REDISEARCH.getHost();
        port = REDISEARCH.getFirstMappedPort();
        client = RediSearchClient.create(RedisURI.create(host, port));
        connection = client.connect();
        sync = connection.sync();
        async = connection.async();
        reactive = connection.reactive();
    }

    @AfterAll
    protected static void teardown() {
        if (connection != null) {
            connection.close();
        }
        if (client != null) {
            client.shutdown();
        }
    }


    protected static Map<String, String> mapOf(String... keyValues) {
        Map<String, String> map = new HashMap<>();
        for (int index = 0; index < keyValues.length / 2; index++) {
            map.put(keyValues[index], keyValues[index + 1]);
        }
        return map;
    }

    protected static List<Map<String, String>> beers() throws IOException {
        List<Map<String, String>> beers = new ArrayList<>();
        CsvSchema schema = CsvSchema.builder().setUseHeader(true).setNullValue("").build();
        CsvMapper mapper = new CsvMapper();
        InputStream inputStream = AbstractBaseTest.class.getClassLoader().getResourceAsStream("beers.csv");
        mapper.readerFor(Map.class).with(schema).readValues(inputStream).forEachRemaining(e -> beers.add((Map) e));
        return beers;
    }

    protected static List<Map<String, String>> createBeerIndex() throws IOException {
        sync.flushall();
        List<Map<String, String>> beers = beers();
        sync.create(INDEX, CreateOptions.<String,String>builder().payloadField(NAME).build(), SCHEMA);
        async.setAutoFlushCommands(false);
        List<RedisFuture<?>> futures = new ArrayList<>();
        for (Map<String, String> beer : beers) {
            futures.add(async.hmset("beer:" + beer.get(ID), beer));
        }
        async.flushCommands();
        async.setAutoFlushCommands(true);
        LettuceFutures.awaitAll(RedisURI.DEFAULT_TIMEOUT_DURATION, futures.toArray(new RedisFuture[0]));
        return beers;
    }

    protected static void createBeerSuggestions() throws IOException {
        List<Map<String, String>> beers = beers();
        async.setAutoFlushCommands(false);
        List<RedisFuture<?>> futures = new ArrayList<>();
        for (Map<String, String> beer : beers) {
            futures.add(async.sugadd(SUGINDEX, Suggestion.builder(beer.get(NAME)).score(1d).build(), false));
        }
        async.flushCommands();
        async.setAutoFlushCommands(true);
        LettuceFutures.awaitAll(RedisURI.DEFAULT_TIMEOUT_DURATION, futures.toArray(new RedisFuture[0]));
    }


}
