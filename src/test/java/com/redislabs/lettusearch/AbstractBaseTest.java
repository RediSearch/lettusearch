package com.redislabs.lettusearch;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.redislabs.lettusearch.index.Schema;
import com.redislabs.lettusearch.index.field.NumericField;
import com.redislabs.lettusearch.index.field.PhoneticMatcher;
import com.redislabs.lettusearch.index.field.TagField;
import com.redislabs.lettusearch.index.field.TextField;
import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.suggest.Suggestion;
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
import java.util.List;

@Testcontainers
public abstract class AbstractBaseTest {

    protected final static String SUGINDEX = "beersSug";

    public final static String ABV = "abv";
    public final static String ID = "id";
    public final static String NAME = "name";
    public final static String STYLE = "style";
    public final static Schema<String> SCHEMA = Schema.<String>builder().field(TextField.<String>builder().name(NAME).matcher(PhoneticMatcher.English).build()).field(TagField.<String>builder().name(STYLE).sortable(true).build()).field(NumericField.<String>builder().name(ABV).sortable(true).build()).build();
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

    protected static List<Document<String,String>> beers() throws IOException {
        List<Document<String, String>> beers = new ArrayList<>();
        CsvSchema schema = CsvSchema.builder().setUseHeader(true).setNullValue("").build();
        CsvMapper mapper = new CsvMapper();
        InputStream inputStream = AbstractBaseTest.class.getClassLoader().getResourceAsStream("beers.csv");
        MappingIterator<Document<String, String>> iterator = mapper.readerFor(Document.class).with(schema).readValues(inputStream);
        iterator.forEachRemaining(b -> {
            if (b.get(ABV) != null) {
                b.setId(b.get(ID));
                b.setScore(1d);
                b.setPayload(b.get(NAME));
                beers.add(b);
            }
        });
        return beers;
    }

    protected static List<Document<String, String>> createBeerIndex() throws IOException {
        sync.flushall();
        List<Document<String, String>> beers = beers();
        sync.create(INDEX, SCHEMA);
        async.setAutoFlushCommands(false);
        List<RedisFuture<?>> futures = new ArrayList<>();
        for (Document<String, String> beer : beers) {
            futures.add(async.add(INDEX, beer));
        }
        async.flushCommands();
        async.setAutoFlushCommands(true);
        LettuceFutures.awaitAll(RedisURI.DEFAULT_TIMEOUT_DURATION, futures.toArray(new RedisFuture[0]));
        return beers;
    }

    protected static List<Document<String, String>> createBeerSuggestions() throws IOException {
        sync.flushall();
        List<Document<String, String>> beers = beers();
        async.setAutoFlushCommands(false);
        List<RedisFuture<?>> futures = new ArrayList<>();
        for (Document<String, String> beer : beers) {
            futures.add(async.sugadd(SUGINDEX, Suggestion.<String>builder().string(beer.get(NAME)).score(1d).build(), false));
        }
        async.flushCommands();
        async.setAutoFlushCommands(true);
        LettuceFutures.awaitAll(RedisURI.DEFAULT_TIMEOUT_DURATION, futures.toArray(new RedisFuture[0]));
        return beers;
    }


}
