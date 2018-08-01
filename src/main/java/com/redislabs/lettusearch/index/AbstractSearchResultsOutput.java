package com.redislabs.lettusearch.index;

import java.nio.ByteBuffer;

import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.output.CommandOutput;

public class AbstractSearchResultsOutput<K, V, S extends AbstractSearchResults<K, V>> extends CommandOutput<K, V, S> {

	protected SearchResult<K, V> current;

	public AbstractSearchResultsOutput(RedisCodec<K, V> codec, S results) {
		super(codec, results);
	}

	@Override
	public void set(ByteBuffer bytes) {
		current = new SearchResult<>();
		current.setDocumentId(codec.decodeKey(bytes));
		output.getResults().add(current);
	}

	@Override
	public void set(long integer) {
		output.setCount(integer);
	}

}
