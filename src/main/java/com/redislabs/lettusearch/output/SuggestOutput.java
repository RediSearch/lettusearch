package com.redislabs.lettusearch.output;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.redislabs.lettusearch.Suggestion;
import com.redislabs.lettusearch.SuggetOptions;

import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.output.CommandOutput;

public class SuggestOutput<K, V> extends CommandOutput<K, V, List<Suggestion<V>>> {

	private Suggestion<V> current;
	private final SuggetOptions options;

	public SuggestOutput(RedisCodec<K, V> codec, SuggetOptions options) {
		super(codec, new ArrayList<>());
		this.options = options;
	}

	@Override
	public void set(ByteBuffer bytes) {
		if (current == null) {
			current = new Suggestion<>();
			if (bytes != null) {
				current.setString(codec.decodeValue(bytes));
			}
			output.add(current);
			if (!options.isWithScores() && !options.isWithPayloads()) {
				current = null;
			}
		} else {
			if (current.getPayload() == null && options.isWithPayloads()) {
				if (bytes != null) {
					current.setPayload(codec.decodeValue(bytes));
				}
				current = null;
			}
		}
	}

	@Override
	public void set(double number) {
		if (current.getScore() == null && options.isWithScores()) {
			current.setScore(number);
			if (!options.isWithPayloads()) {
				current = null;
			}
		}
	}

}
