package com.redislabs.lettusearch.aggregate;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.output.CommandOutput;
import io.lettuce.core.output.MapOutput;

public class AggregateOutput<K, V> extends CommandOutput<K, V, AggregateResults<K, V>> {

	private MapOutput<K, V> nested;
	private int mapCount = -1;
	private final List<Integer> counts = new ArrayList<>();

	public AggregateOutput(RedisCodec<K, V> codec) {
		super(codec, new AggregateResults<>());
		nested = new MapOutput<>(codec);
	}

	@Override
	public void set(ByteBuffer bytes) {
		nested.set(bytes);
	}

	@Override
	public void complete(int depth) {

		if (!counts.isEmpty()) {
			int expectedSize = counts.get(0);

			if (nested.get().size() == expectedSize) {
				counts.remove(0);
				output.getResults().add(new LinkedHashMap<>(nested.get()));
				nested.get().clear();
			}
		}
	}

	@Override
	public void set(long integer) {
		output.setCount(integer);
	}

	@Override
	public void multi(int count) {

		nested.multi(count);

		if (mapCount == -1) {
			mapCount = count;
		} else {
			// div 2 because of key value pair counts twice
			counts.add(count / 2);
		}
	}

}
