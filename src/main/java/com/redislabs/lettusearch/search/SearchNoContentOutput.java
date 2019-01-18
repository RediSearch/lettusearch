package com.redislabs.lettusearch.search;

import java.nio.ByteBuffer;

import io.lettuce.core.LettuceStrings;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.output.CommandOutput;

public class SearchNoContentOutput<K, V> extends CommandOutput<K, V, SearchResults<K, V>> {

	private SearchResult<K, V> current;
	private SearchOptions options;

	public SearchNoContentOutput(RedisCodec<K, V> codec, SearchOptions options) {
		super(codec, new SearchResults<>());
		this.options = options;
	}

	@Override
	public void set(ByteBuffer bytes) {
		if (current == null) {
			current = new SearchResult<>();
			current.setDocumentId(codec.decodeKey(bytes));
			output.getResults().add(current);
			if (!options.isWithScores()) {
				current = null;
			}
		} else {
			if (options.isWithScores()) {
				current.setScore(LettuceStrings.toDouble(decodeAscii(bytes)));
			}
			current = null;
		}
	}

	@Override
	public void set(long integer) {
		output.setCount(integer);
	}

}
