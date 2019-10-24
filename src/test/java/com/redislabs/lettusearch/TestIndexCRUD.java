package com.redislabs.lettusearch;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.CreateOptions;
import com.redislabs.lettusearch.search.DropOptions;
import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.search.field.FieldOptions;
import com.redislabs.lettusearch.search.field.FieldType;
import com.redislabs.lettusearch.search.field.TextField;

import static com.redislabs.lettusearch.Beers.*;

import io.lettuce.core.RedisCommandExecutionException;

public class TestIndexCRUD extends AbstractBaseTest {

	@Test
	public void testTemporaryIndex() throws InterruptedException {
		String indexName = "temporaryIndex";
		commands.create(indexName, Schema.builder().field(TextField.builder().name("field1").build()).build(),
				CreateOptions.builder().temporary(1l).build());
		List<Object> info = commands.ftInfo(indexName);
		Assert.assertEquals(indexName, info.get(1));
		Thread.sleep(1000);
		try {
			info = commands.ftInfo(indexName);
		} catch (RedisCommandExecutionException e) {
			Assert.assertEquals("Unknown Index name", e.getMessage());
			return;
		}
		Assert.fail("Temporary index not deleted");
	}

	@Test
	public void testDrop() {
		commands.drop(INDEX, DropOptions.builder().keepDocs(false).build());
		Map<String, String> fields = new HashMap<>();
		fields.put("field1", "value1");
		try {
			commands.add(INDEX, "newDocId", 1, fields, AddOptions.builder().build());
			Assert.fail("Index not dropped");
		} catch (RedisCommandExecutionException e) {
			// ignored, expected behavior
		}
	}

	@Test
	public void testAlter() {
		commands.alter(INDEX, "newField", FieldOptions.builder().type(FieldType.Tag).build());
		Map<String, String> fields = new HashMap<>();
		fields.put("newField", "value1");
		commands.add(INDEX, "newDocId", 1, fields, AddOptions.builder().build());
		SearchResults<String, String> results = commands.search(INDEX, "@newField:{value1}",
				SearchOptions.builder().build());
		Assert.assertEquals(1, results.getCount());
		Assert.assertEquals(fields.get("newField"), results.get(0).get("newField"));
	}

	@Test
	public void testIndexInfo() {
		Map<String, Object> indexInfo = toMap(commands.ftInfo(INDEX));
		Assert.assertEquals(INDEX, indexInfo.get("index_name"));
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
