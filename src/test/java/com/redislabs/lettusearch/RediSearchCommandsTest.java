package com.redislabs.lettusearch;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.redislabs.lettusearch.Field.FieldType;

import io.lettuce.core.RedisClient;
import io.lettuce.core.dynamic.RedisCommandFactory;

public class RediSearchCommandsTest {

	@Test
	public void testSuggestions() {
		String key = "artists";
		RedisClient client = RedisClient.create("redis://localhost:6379");
		RedisCommandFactory factory = new RedisCommandFactory(client.connect());
		RediSearchCommands commands = factory.getCommands(RediSearchCommands.class);
		commands.sugadd(key, "Herbie Hancock", 1.0);
		commands.sugadd(key, "Herbie Mann", 1.0);
		commands.sugadd(key, "DJ Herbie", 1.0);
		List<String> suggestions = commands.suggetFuzzy(key, "Hor");
		for (String suggestion : suggestions) {
			System.out.println(suggestion);
		}
		assertEquals(3, (long) commands.suglen(key));
		commands.sugdel(key, "DJ Herbie");
		assertEquals(2, (long) commands.suglen(key));
		client.shutdown();
	}

	@Test
	public void testCreate() {
		RedisClient client = RedisClient.create("redis://localhost:6379");
		RedisCommandFactory factory = new RedisCommandFactory(client.connect());
		RediSearchCommands commands = factory.getCommands(RediSearchCommands.class);
		SchemaArgs schema = new SchemaArgs();
		schema.fields(new Field("field1", FieldType.TEXT));
		commands.create("testIndex", schema);
		client.shutdown();
	}

}
