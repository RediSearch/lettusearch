package com.redislabs.lettusearch;

import com.redislabs.lettusearch.index.*;
import com.redislabs.lettusearch.index.field.Field;
import com.redislabs.lettusearch.index.field.FieldOptions;
import com.redislabs.lettusearch.index.field.FieldType;
import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.search.SearchResults;
import io.lettuce.core.RedisCommandExecutionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestIndex extends AbstractBaseTest {

    @Test
    public void temporary() throws InterruptedException {
        String indexName = "temporaryIndex";
        sync.create(indexName, Schema.of(Field.text("field1").build()), CreateOptions.<String, String>builder().temporary(1L).build());

        List<Object> info = sync.ftInfo(indexName);
        assertEquals(indexName, info.get(1));
        Thread.sleep(1501);
        try {
            sync.ftInfo(indexName);
        } catch (RedisCommandExecutionException e) {
            assertEquals("Unknown Index name", e.getMessage());
            return;
        }
        fail("Temporary index not deleted");
    }

    @Test
    public void del() throws IOException {
        createBeerIndex();
        boolean deleted = sync.del(INDEX, "1836", true);
        assertTrue(deleted);
        Map<String, String> map = sync.get(INDEX, "1836");
        assertTrue(map.isEmpty());
    }


    @Test
    public void drop() throws IOException {
        createBeerIndex();
        sync.drop(INDEX, DropOptions.builder().keepDocs(false).build());
        Document<String, String> doc = Document.<String, String>builder().id("newDocId").score(1d).build();
        doc.put("field1", "value1");
        try {
            sync.add(INDEX, doc);
            fail("Index not dropped");
        } catch (RedisCommandExecutionException e) {
            // ignored, expected behavior
        }
    }

    @Test
    public void alter() throws IOException {
        createBeerIndex();
        sync.alter(INDEX, "newField", FieldOptions.builder().type(FieldType.TAG).build());
        Document<String, String> doc = Document.<String, String>builder().id("newDocId").score(1d).build();
        doc.put("newField", "value1");
        sync.add(INDEX, doc);
        SearchResults<String, String> results = sync.search(INDEX, "@newField:{value1}");
        assertEquals(1, results.getCount());
        assertEquals(doc.get("newField"), results.get(0).get("newField"));
    }

    @Test
    public void info() throws IOException {
        createBeerIndex();
        Map<String, Object> indexInfo = toMap(sync.ftInfo(INDEX));
        assertEquals(INDEX, indexInfo.get("index_name"));
    }

    @Test
    public void testCreateOptions() {
        CreateOptions<String, String> options = CreateOptions.<String, String>builder().prefixes("release:").payloadField("xml").build();
        Schema<String> schema = Schema.of(Field.text("artist").sortable(true).build(), Field.tag("id").sortable(true).build(), Field.text("title").sortable(true).build());
        sync.create("releases", schema, options);
        IndexInfo<String> info = RediSearchUtils.getInfo(sync.ftInfo("releases"));
        Assertions.assertEquals(schema.getFields().size(), info.getFields().size());

    }

    private Map<String, Object> toMap(List<Object> indexInfo) {
        Map<String, Object> map = new HashMap<>();
        Iterator<Object> iterator = indexInfo.iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            map.put(key, iterator.next());
        }
        return map;
    }

    @Test
    public void createOnHash() throws IOException {
        List<Document<String, String>> beers = createBeerIndex();
        String indexName = "hashIndex";
        sync.create(indexName, SCHEMA, CreateOptions.<String, String>builder().on(Structure.HASH).prefixes("beer:").build());
        for (Document<String, String> beer : beers) {
            sync.hmset("beer:" + beer.get(ID), beer);
        }
        IndexInfo<String> info = RediSearchUtils.getInfo(sync.ftInfo(indexName));
        Double numDocs = info.getNumDocs();
        assertEquals(beers.size(), numDocs);
    }

    @Test
    public void list() {
        sync.flushall();
        List<String> indexNames = Arrays.asList("index1", "index2", "index3");
        for (String indexName : indexNames) {
            sync.create(indexName, Schema.of(Field.text("field1").sortable(true).build()));
        }
        List<String> list = sync.list();
        assertEquals(new HashSet<>(indexNames), new HashSet<>(list));
    }

}
