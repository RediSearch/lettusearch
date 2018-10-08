package com.redislabs.lettusearch;

import com.redislabs.lettusearch.index.SearchResultsNoContentOutput;
import com.redislabs.lettusearch.index.SearchResultsOutput;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.dynamic.RedisCommandFactory;
import io.lettuce.core.dynamic.output.OutputRegistry;
import io.lettuce.core.dynamic.output.OutputRegistryCommandOutputFactoryResolver;

public class RediSearchClient {

	private RediSearchCommands commands;

	public RediSearchClient(RedisCommandFactory factory) {
		OutputRegistry outputRegistry = new OutputRegistry();
		outputRegistry.register(SearchResultsOutput.class, SearchResultsOutput::new);
		outputRegistry.register(SearchResultsNoContentOutput.class, SearchResultsNoContentOutput::new);
		factory.setCommandOutputFactoryResolver(new OutputRegistryCommandOutputFactoryResolver(outputRegistry));
		this.commands = factory.getCommands(RediSearchCommands.class);
	}

	public RediSearchClient(StatefulRedisConnection<String, String> connection) {
		this(new RedisCommandFactory(connection));
	}

	public RediSearchCommands getCommands() {
		return commands;
	}

}
