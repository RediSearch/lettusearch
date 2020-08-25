package com.redislabs.lettusearch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.redislabs.lettusearch.index.IndexInfo;
import com.redislabs.lettusearch.index.Schema;
import com.redislabs.lettusearch.index.field.Field;
import com.redislabs.lettusearch.index.field.TagField;
import com.redislabs.lettusearch.index.field.TextField;
import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.search.SearchResults;

import io.lettuce.core.RedisURI;

public class TestUtils extends AbstractBaseTest {

	@Test
	public void ftInfo() {
		List<Object> infoList = sync.ftInfo(Beers.INDEX);
		IndexInfo<String> info = RediSearchUtils.getInfo(infoList);
		assertEquals((Long) 2348L, info.getNumDocs());
		List<Field<String>> fields = info.getFields();
		TextField<String> nameField = (TextField<String>) fields.get(0);
		assertEquals(Beers.NAME, nameField.getName());
		assertEquals(false, nameField.isNoIndex());
		assertEquals(false, nameField.isNoStem());
		assertEquals(false, nameField.isSortable());
		TagField<String> styleField = (TagField<String>) fields.get(1);
		assertEquals(Beers.STYLE, styleField.getName());
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
	public void escapeTag() {
		String index = "escapeTagTestIdx";
		String idField = "id";
		sync.create(index, Schema.<String>builder().field(TagField.<String>builder().name(idField).build()).build());
		sync.add(index, Document.<String, String>builder().id("doc1")
				.field(idField, "chris@blah.org,User1#test.org,usersdfl@example.com").build());
		SearchResults<String, String> results = sync.search(index,
				"@id:{" + RediSearchUtils.escapeTag("User1#test.org") + "}");
		assertEquals(1, results.size());
	}
}
