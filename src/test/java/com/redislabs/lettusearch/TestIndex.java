package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.Beers.INDEX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.redislabs.lettusearch.index.CreateOptions;
import com.redislabs.lettusearch.index.DropOptions;
import com.redislabs.lettusearch.index.IndexInfo;
import com.redislabs.lettusearch.index.Schema;
import com.redislabs.lettusearch.index.Structure;
import com.redislabs.lettusearch.index.field.FieldOptions;
import com.redislabs.lettusearch.index.field.FieldType;
import com.redislabs.lettusearch.index.field.TagField;
import com.redislabs.lettusearch.index.field.TextField;
import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.search.SearchResults;

import io.lettuce.core.RedisCommandExecutionException;

public class TestIndex extends AbstractBaseTest {

	@Test
	public void temporary() throws InterruptedException {
		String indexName = "temporaryIndex";
		sync.create(indexName,
				Schema.<String>builder().field(TextField.<String>builder().name("field1").build()).build(),
				CreateOptions.<String, String>builder().temporary(1L).build());
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
	public void drop() {
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
	public void alter() {
		sync.alter(INDEX, "newField", FieldOptions.builder().type(FieldType.Tag).build());
		Document<String, String> doc = Document.<String, String>builder().id("newDocId").score(1d).build();
		doc.put("newField", "value1");
		sync.add(INDEX, doc);
		SearchResults<String, String> results = sync.search(INDEX, "@newField:{value1}");
		assertEquals(1, results.getCount());
		assertEquals(doc.get("newField"), results.get(0).get("newField"));
	}

	@Test
	public void info() {
		Map<String, Object> indexInfo = toMap(sync.ftInfo(INDEX));
		assertEquals(INDEX, indexInfo.get("index_name"));
	}

	@Test
	public void testCreateOptions() {
		CreateOptions<String, String> options = CreateOptions.<String, String>builder().prefixes("release:")
				.payloadField("xml").build();
		Schema<String> schema = Schema.<String>builder()
				.field(TextField.<String>builder().name("artist").sortable(true).build())
				.field(TagField.<String>builder().name("id").sortable(true).build())
				.field(TextField.<String>builder().name("title").sortable(true).build()).build();
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
	public void createOnHash() throws InterruptedException {
		String indexName = "hashIndex";
		sync.create(indexName, Beers.SCHEMA,
				CreateOptions.<String, String>builder().on(Structure.HASH).prefixes("beer:").build());
		for (Document<String, String> beer : beers) {
			sync.hmset("beer:" + beer.get(Beers.ID), beer);
		}
		IndexInfo<String> info = RediSearchUtils.getInfo(sync.ftInfo(indexName));
		Double numDocs = info.getNumDocs();
		assertEquals(beers.size(), numDocs);
	}

}
