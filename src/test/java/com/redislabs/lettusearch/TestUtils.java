package com.redislabs.lettusearch;

import io.lettuce.core.RedisURI;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class TestUtils extends AbstractBaseTest {

    @Test
    public void ftInfo() throws IOException, ExecutionException, InterruptedException {
        createBeerIndex();
        List<Object> infoList = async.ftInfo(INDEX).get();
        IndexInfo<String> info = RediSearchUtils.getInfo(infoList);
        assertEquals(2348, info.getNumDocs());
        List<Field<String>> fields = info.getFields();
        Field.Text<String> nameField = (Field.Text<String>) fields.get(0);
        assertEquals(NAME, nameField.getName());
        assertFalse(nameField.isNoIndex());
        assertFalse(nameField.isNoStem());
        assertFalse(nameField.isSortable());
        Field.Tag<String> styleField = (Field.Tag<String>) fields.get(1);
        assertEquals(STYLE, styleField.getName());
        assertTrue(styleField.isSortable());
        assertEquals(",", styleField.getSeparator());
    }

    public void enterpriseFtInfo() {
        RediSearchClient client = RediSearchClient.create(RedisURI.create("redis-15322.jrx.demo.redislabs.com", 15322));
        RediSearchCommands<String, String> commands = client.connect().sync();
        List<Object> indexInfo = commands.ftInfo("IDXUTSEARCH");
        RediSearchUtils.getInfo(indexInfo);
    }

    @Test
    public void escapeTag() throws ExecutionException, InterruptedException {
        String index = "escapeTagTestIdx";
        String idField = "id";
        async.create(index, Field.tag(idField).build()).get();
        Map<String, String> doc1 = new HashMap<>();
        doc1.put(idField, "chris@blah.org,User1#test.org,usersdfl@example.com");
        async.hmset("doc1", doc1).get();
        SearchResults<String, String> results = async.search(index, "@id:{" + RediSearchUtils.escapeTag("User1#test.org") + "}").get();
        assertEquals(1, results.size());
    }
}
