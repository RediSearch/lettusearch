package com.redislabs.lettusearch.output;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.redislabs.lettusearch.suggest.SuggestGetOptions;
import com.redislabs.lettusearch.suggest.SuggestResult;

import io.lettuce.core.LettuceStrings;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.output.CommandOutput;

public class SuggestOutput<K, V> extends CommandOutput<K, V, List<SuggestResult<V>>> {

	private SuggestResult<V> current;
	private SuggestGetOptions options;

	public SuggestOutput(RedisCodec<K, V> codec, SuggestGetOptions options) {
		super(codec, new ArrayList<>());
		this.options = options;
	}

	@Override
	public void set(ByteBuffer bytes) {
		if (current == null) {
			current = new SuggestResult<>();
			if (bytes != null) {
				current.string(codec.decodeValue(bytes));
			}
			output.add(current);
			if (!options.withScores() && !options.withPayloads()) {
				current = null;
			}
		} else {
			if (current.score() == null && options.withScores()) {
				if (bytes != null) {
					current.score(LettuceStrings.toDouble(decodeAscii(bytes)));
				}
				if (!options.withPayloads()) {
					current = null;
				}
			} else {
				if (current.payload() == null && options.withPayloads()) {
					if (bytes != null) {
						current.payload(codec.decodeValue(bytes));
					}
					current = null;
				}
			}
		}
	}

}
