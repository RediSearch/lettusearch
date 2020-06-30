package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.Beers.INDEX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.redislabs.lettusearch.index.CreateOptions;
import com.redislabs.lettusearch.index.DropOptions;
import com.redislabs.lettusearch.index.Schema;
import com.redislabs.lettusearch.index.field.FieldOptions;
import com.redislabs.lettusearch.index.field.FieldType;
import com.redislabs.lettusearch.index.field.TextField;
import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.search.SearchResults;

import io.lettuce.core.RedisCommandExecutionException;

public class TestIndex extends AbstractBaseTest {

    @Test
    public void temporary() throws InterruptedException {
        String indexName = "temporaryIndex";
        commands.create(indexName, Schema.builder().field(TextField.builder().name("field1").build()).build(), CreateOptions.builder().temporary(1L).build());
        List<Object> info = commands.ftInfo(indexName);
        assertEquals(indexName, info.get(1));
        Thread.sleep(1001);
        try {
            commands.ftInfo(indexName);
        } catch (RedisCommandExecutionException e) {
            assertEquals("Unknown Index name", e.getMessage());
            return;
        }
        fail("Temporary index not deleted");
    }

    @Test
    public void drop() {
        commands.drop(INDEX, DropOptions.builder().keepDocs(false).build());
        Document<String, String> doc = Document.<String, String>builder().id("newDocId").score(1d).build();
        doc.put("field1", "value1");
        try {
            commands.add(INDEX, doc, null);
            fail("Index not dropped");
        } catch (RedisCommandExecutionException e) {
            // ignored, expected behavior
        }
    }

    @Test
    public void alter() {
        commands.alter(INDEX, "newField", FieldOptions.builder().type(FieldType.Tag).build());
        Document<String, String> doc = Document.<String, String>builder().id("newDocId").score(1d).build();
        doc.put("newField", "value1");
        commands.add(INDEX, doc, null);
        SearchResults<String, String> results = commands.search(INDEX, "@newField:{value1}");
        assertEquals(1, results.getCount());
        assertEquals(doc.get("newField"), results.get(0).get("newField"));
    }

    @Test
    public void info() {
        Map<String, Object> indexInfo = toMap(commands.ftInfo(INDEX));
        assertEquals(INDEX, indexInfo.get("index_name"));
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

}
