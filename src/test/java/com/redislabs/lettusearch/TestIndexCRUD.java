package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.Beers.INDEX;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.redislabs.lettusearch.search.CreateOptions;
import com.redislabs.lettusearch.search.DropOptions;
import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.search.field.Field;
import com.redislabs.lettusearch.search.field.FieldOptions;
import com.redislabs.lettusearch.search.field.FieldType;

import io.lettuce.core.RedisCommandExecutionException;

public class TestIndexCRUD extends AbstractBaseTest {

	@Test
	public void testTemporaryIndex() throws InterruptedException {
		String indexName = "temporaryIndex";
		commands.create(indexName, new Schema().field(Field.text("field1")), new CreateOptions().temporary(1l));
		List<Object> info = commands.indexInfo(indexName);
		Assert.assertEquals(indexName, info.get(1));
		Thread.sleep(1001);
		try {
			info = commands.indexInfo(indexName);
		} catch (RedisCommandExecutionException e) {
			Assert.assertEquals("Unknown Index name", e.getMessage());
			return;
		}
		Assert.fail("Temporary index not deleted");
	}

	@Test
	public void testDrop() {
		commands.drop(INDEX, new DropOptions().keepDocs(false));
		Map<String, String> fields = new HashMap<>();
		fields.put("field1", "value1");
		try {
			commands.add(INDEX, "newDocId", 1, fields);
			Assert.fail("Index not dropped");
		} catch (RedisCommandExecutionException e) {
			// ignored, expected behavior
		}
	}

	@Test
	public void testAlter() {
		commands.alter(INDEX, "newField", new FieldOptions().type(FieldType.Tag));
		Map<String, String> fields = new HashMap<>();
		fields.put("newField", "value1");
		commands.add(INDEX, "newDocId", 1, fields);
		SearchResults<String, String> results = commands.search(INDEX, "@newField:{value1}");
		Assert.assertEquals(1, results.getCount());
		Assert.assertEquals(fields.get("newField"), results.get(0).get("newField"));
	}

	@Test
	public void testIndexInfo() {
		Map<String, Object> indexInfo = toMap(commands.indexInfo(INDEX));
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
