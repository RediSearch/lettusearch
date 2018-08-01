package com.redislabs.lettusearch.index;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import io.lettuce.core.LettuceStrings;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.output.MapOutput;

public class SearchResultsOutput<K, V> extends AbstractSearchResultsOutput<K, V, SearchResults<K, V>> {

	private MapOutput<K, V> nested;
	private int mapCount = -1;
	private final List<Integer> counts = new ArrayList<>();

	public SearchResultsOutput(RedisCodec<K, V> codec) {
		super(codec, new SearchResults<>());
		nested = new MapOutput<>(codec);
	}

	@Override
	public void set(long integer) {
		output.setCount(integer);
	}

	@Override
	public void set(ByteBuffer bytes) {
		if (current == null) {
			super.set(bytes);
		} else {
			if (current.getScore() == null) {
				current.setScore(LettuceStrings.toDouble(decodeAscii(bytes)));
			} else {
				nested.set(bytes);
			}
		}
	}

	@Override
	public void complete(int depth) {
		if (!counts.isEmpty()) {
			if (nested.get().size() == counts.get(0)) {
				counts.remove(0);
				current.setFields(nested.get());
				nested = new MapOutput<>(codec);
				current = null;
			}
		}
	}

	@Override
	public void multi(int count) {
		nested.multi(count);
		if (mapCount == -1) {
			// subtract one because #docs is first
			// div 2 because of docId/doc pair counts twice
			mapCount = (count - 1) / 2;
		} else {
			// div 2 because of key value pair counts twice
			counts.add(count / 2);
		}
	}

}
