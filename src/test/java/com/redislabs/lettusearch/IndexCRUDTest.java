package com.redislabs.lettusearch;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.DropOptions;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.search.field.FieldOptions;
import com.redislabs.lettusearch.search.field.FieldType;

import io.lettuce.core.RedisCommandExecutionException;

public class IndexCRUDTest extends AbstractBaseTest {

	@Test
	public void testDrop() {
		connection.sync().drop(INDEX, DropOptions.builder().keepDocs(false).build());
		Map<String, String> fields = new HashMap<>();
		fields.put("field1", "value1");
		try {
			connection.sync().add(INDEX, "newDocId", 1, fields, AddOptions.builder().build());
			Assert.fail("Index not dropped");
		} catch (RedisCommandExecutionException e) {
			// ignored, expected behavior
		}
	}

	@Test
	public void testAlter() {
		connection.sync().alter(INDEX, "newField", FieldOptions.builder().type(FieldType.Tag).build());
		Map<String, String> fields = new HashMap<>();
		fields.put("newField", "value1");
		connection.sync().add(INDEX, "newDocId", 1, fields, AddOptions.builder().build());
		SearchResults<String, String> results = connection.sync().search(INDEX, "@newField:{value1}",
				SearchOptions.builder().build());
		Assert.assertEquals(1, results.getCount());
		Assert.assertEquals(fields.get("newField"), results.getResults().get(0).getFields().get("newField"));
	}

	@Test
	public void testIndexInfo() {
		Map<String, Object> indexInfo = toMap(connection.sync().indexInfo(INDEX));
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
