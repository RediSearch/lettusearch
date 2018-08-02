package com.redislabs.lettusearch;

import com.redislabs.lettusearch.index.SearchResultsNoContentOutput;
import com.redislabs.lettusearch.index.SearchResultsOutput;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.dynamic.RedisCommandFactory;
import io.lettuce.core.dynamic.output.OutputRegistry;
import io.lettuce.core.dynamic.output.OutputRegistryCommandOutputFactoryResolver;

public class RediSearchConnection<K, V> {

	private StatefulRedisConnection<K, V> connection;

	public RediSearchConnection(StatefulRedisConnection<K, V> connection) {
		this.connection = connection;
	}

	@SuppressWarnings("unchecked")
	public RediSearchCommands<K, V> search() {
		RedisCommandFactory factory = new RedisCommandFactory(connection);
		OutputRegistry outputRegistry = new OutputRegistry();
		outputRegistry.register(SearchResultsOutput.class, SearchResultsOutput::new);
		outputRegistry.register(SearchResultsNoContentOutput.class, SearchResultsNoContentOutput::new);
		factory.setCommandOutputFactoryResolver(new OutputRegistryCommandOutputFactoryResolver(outputRegistry));
		return factory.getCommands(RediSearchCommands.class);
	}

	public StatefulRedisConnection<K, V> getRedisConnection() {
		return connection;
	}

}
