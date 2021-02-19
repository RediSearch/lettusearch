package com.redislabs.lettusearch;

import io.lettuce.core.RedisURI;

import javax.xml.validation.Schema;

public class TestClient {

    public static void main(String[] args) {
        RedisURI redisUri = RedisURI.Builder.sentinel("127.0.0.1", 17000, "redis-cluster").withSentinel("127.0.0.1", 17001).withSentinel("127.0.0.1", 17002).build();
        RediSearchClient client = RediSearchClient.create(redisUri);
        StatefulRediSearchConnection<String, String> connection = client.connect();
        connection.sync().create("testIndex", Field.text("field1").build());
        System.out.println("Success");
    }

}
