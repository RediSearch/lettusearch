package com.redislabs.lettusearch.api;

import java.nio.ByteBuffer;

import io.lettuce.core.LettuceStrings;
import io.lettuce.core.codec.RedisCodec;

public class SearchResultsNoContentOutput<K, V>
		extends AbstractSearchResultsOutput<K, V, SearchResultsNoContent<K, V>> {

	public SearchResultsNoContentOutput(RedisCodec<K, V> codec) {
		super(codec, new SearchResultsNoContent<>());
	}

	@Override
	public void set(ByteBuffer bytes) {
		if (current == null) {
			super.set(bytes);
		} else {
			current.setScore(LettuceStrings.toDouble(decodeAscii(bytes)));
			current = null;
		}
	}
}
