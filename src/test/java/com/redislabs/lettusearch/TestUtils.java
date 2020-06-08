package com.redislabs.lettusearch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.redislabs.lettusearch.index.IndexInfo;
import com.redislabs.lettusearch.index.field.Field;
import com.redislabs.lettusearch.index.field.TagField;
import com.redislabs.lettusearch.index.field.TextField;

import io.lettuce.core.RedisURI;

public class TestUtils extends AbstractBaseTest {

	@Test
	public void ftInfo() {
		List<Object> infoList = commands.ftInfo(Beers.INDEX);
		IndexInfo info = RediSearchUtils.getInfo(infoList);
		assertEquals((Long) 2348L, info.getNumDocs());
		List<Field> fields = info.getFields();
		TextField nameField = (TextField) fields.get(0);
		assertEquals(Beers.NAME, nameField.getName());
		assertEquals(false, nameField.isNoIndex());
		assertEquals(false, nameField.isNoStem());
		assertEquals(false, nameField.isSortable());
		TagField styleField = (TagField) fields.get(1);
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

}
