package com.redislabs.lettusearch;

import com.redislabs.lettusearch.index.Schema;
import com.redislabs.lettusearch.index.field.TextField;

import io.lettuce.core.RedisURI;

public class TestClient {

	public static void main(String[] args) {
		RedisURI redisUri = RedisURI.Builder.sentinel("127.0.0.1", 17000, "redis-cluster")
				.withSentinel("127.0.0.1", 17001).withSentinel("127.0.0.1", 17002).build();
		RediSearchClient client = RediSearchClient.create(redisUri);
		StatefulRediSearchConnection<String, String> connection = client.connect();
		connection.sync().create("testIndex",
				Schema.<String>builder().field(TextField.<String>builder().name("field1").build()).build());
		System.out.println("Success");
	}

}
