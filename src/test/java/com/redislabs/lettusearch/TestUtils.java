package com.redislabs.lettusearch;

import com.redislabs.lettusearch.index.IndexInfo;
import com.redislabs.lettusearch.index.Schema;
import com.redislabs.lettusearch.index.field.Field;
import com.redislabs.lettusearch.index.field.TagField;
import com.redislabs.lettusearch.index.field.TextField;
import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.search.SearchResults;
import io.lettuce.core.RedisURI;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtils extends AbstractBaseTest {

	@Test
	public void ftInfo() throws IOException, ExecutionException, InterruptedException {
		createBeerIndex();
		List<Object> infoList = async.ftInfo(INDEX).get();
		IndexInfo<String> info = RediSearchUtils.getInfo(infoList);
		assertEquals(2348, info.getNumDocs());
		List<Field<String>> fields = info.getFields();
		TextField<String> nameField = (TextField<String>) fields.get(0);
		assertEquals(NAME, nameField.getName());
		assertEquals(false, nameField.isNoIndex());
		assertEquals(false, nameField.isNoStem());
		assertEquals(false, nameField.isSortable());
		TagField<String> styleField = (TagField<String>) fields.get(1);
		assertEquals(STYLE, styleField.getName());
		assertEquals(true, styleField.isSortable());
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
		async.create(index, Schema.of(Field.tag(idField).build())).get();
		async.add(index, Document.<String, String>builder().id("doc1")
				.field(idField, "chris@blah.org,User1#test.org,usersdfl@example.com").build()).get();
		SearchResults<String, String> results = async.search(index,
				"@id:{" + RediSearchUtils.escapeTag("User1#test.org") + "}").get();
		assertEquals(1, results.size());
	}
}
