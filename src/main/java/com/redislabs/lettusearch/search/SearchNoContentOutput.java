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
			if (bytes != null) {
				current.setDocumentId(codec.decodeKey(bytes));
			}
			output.add(current);
			if (!options.withScores()) {
				current = null;
			}
		} else {
			if (options.withScores()) {
				if (bytes != null) {
					current.setScore(LettuceStrings.toDouble(decodeAscii(bytes)));
				}
			}
			current = null;
		}
	}

	@Override
	public void set(long integer) {
		output.setCount(integer);
	}

}
