package com.redislabs.lettusearch.output;

import java.nio.ByteBuffer;

import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.search.SearchResults;

import io.lettuce.core.LettuceStrings;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.output.CommandOutput;

public class SearchNoContentOutput<K, V> extends CommandOutput<K, V, SearchResults<K, V>> {

	private Document<K, V> current;
	private boolean withScores;

	public SearchNoContentOutput(RedisCodec<K, V> codec, boolean withScores) {
		super(codec, new SearchResults<>());
		this.withScores = withScores;
	}

	@Override
	public void set(ByteBuffer bytes) {
		if (current == null) {
			current = new Document<>();
			if (bytes != null) {
				current.setId(codec.decodeKey(bytes));
			}
			output.add(current);
			if (!withScores) {
				current = null;
			}
		} else {
			if (withScores) {
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
