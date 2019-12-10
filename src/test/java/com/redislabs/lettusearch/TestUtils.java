package com.redislabs.lettusearch;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.redislabs.lettusearch.RediSearchUtils.IndexInfo;
import com.redislabs.lettusearch.search.field.Field;
import com.redislabs.lettusearch.search.field.TagField;
import com.redislabs.lettusearch.search.field.TextField;

import io.lettuce.core.RedisURI;

public class TestUtils extends AbstractBaseTest {

	@Test
	public void testFtInfo() {
		List<Object> infoList = commands.indexInfo(Beers.INDEX);
		IndexInfo info = RediSearchUtils.getInfo(infoList);
		Assert.assertEquals((Long) 2348l, info.numDocs());
		List<Field> fields = info.fields();
		TextField nameField = (TextField) fields.get(0);
		Assert.assertEquals(Beers.FIELD_NAME, nameField.name());
		Assert.assertEquals(false, nameField.noIndex());
		Assert.assertEquals(false, nameField.noStem());
		Assert.assertEquals(false, nameField.sortable());
		TagField styleField = (TagField) fields.get(1);
		Assert.assertEquals(Beers.FIELD_STYLE, styleField.name());
		Assert.assertEquals(true, styleField.sortable());
		Assert.assertEquals(",", styleField.separator());
	}

	public void testEnterpriseFtInfo() {
		RediSearchClient client = RediSearchClient.create(RedisURI.create("redis-15322.jrx.demo.redislabs.com", 15322));
		RediSearchCommands<String, String> commands = client.connect().sync();
		List<Object> indexInfo = commands.indexInfo("IDXUTSEARCH");
		RediSearchUtils.getInfo(indexInfo);
	}

}
