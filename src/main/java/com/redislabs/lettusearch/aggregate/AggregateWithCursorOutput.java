package com.redislabs.lettusearch.aggregate;

import java.nio.ByteBuffer;

import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.output.CommandOutput;

public class AggregateWithCursorOutput<K, V> extends CommandOutput<K, V, AggregateWithCursorResults<K, V>> {

	private AggregateOutput<K, V> nested;
	private int count = -1;

	public AggregateWithCursorOutput(RedisCodec<K, V> codec) {
		super(codec, new AggregateWithCursorResults<>());
		nested = new AggregateOutput<>(codec);
	}

	@Override
	public void set(ByteBuffer bytes) {
		nested.set(bytes);
	}

	@Override
	public void complete(int depth) {
		nested.complete(depth);
	}

	@Override
	public void set(long integer) {
		if (nested.getMapCount() == nested.get().getResults().size()) {
			output.setResults(nested.get());
			output.setCursor(integer);
		} else {
			nested.set(integer);
		}
	}

	@Override
	public void multi(int count) {
		if (this.count == -1) {
			this.count = count;
		} else {
			nested.multi(count);
		}
	}

}
