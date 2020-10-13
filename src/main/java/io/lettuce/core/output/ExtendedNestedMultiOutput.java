package io.lettuce.core.output;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.internal.LettuceFactories;

/**
 * {@link List} of command outputs, possibly deeply nested. Decodes simple
 * strings through {@link StringCodec#UTF8}.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Will Glozer
 * @author Mark Paluch
 */
public class ExtendedNestedMultiOutput<K, V> extends CommandOutput<K, V, List<Object>> {

	private final Deque<List<Object>> stack;

	private int depth;

	private boolean initialized;

	public ExtendedNestedMultiOutput(RedisCodec<K, V> codec) {
		super(codec, Collections.emptyList());
		stack = LettuceFactories.newSpScQueue();
		depth = 0;
	}

	@Override
	public void set(long integer) {

		if (!initialized) {
			output = new ArrayList<>();
		}

		output.add(integer);
	}

	@Override
	public void set(double number) {
		if (!initialized) {
			output = new ArrayList<>();
		}

		output.add(number);
	}

	@Override
	public void set(ByteBuffer bytes) {

		if (!initialized) {
			output = new ArrayList<>();
		}

		output.add(bytes == null ? null : codec.decodeValue(bytes));
	}

	@Override
	public void setSingle(ByteBuffer bytes) {

		if (!initialized) {
			output = new ArrayList<>();
		}

		output.add(bytes == null ? null : StringCodec.UTF8.decodeValue(bytes));
	}

	@Override
	public void complete(int depth) {
		if (depth > 0 && depth < this.depth) {
			output = stack.pop();
			this.depth--;
		}
	}

	@Override
	public void multi(int count) {

		if (!initialized) {
			output = OutputFactory.newList(Math.max(1, count));
			initialized = true;
		}

		List<Object> a = OutputFactory.newList(count);
		output.add(a);
		stack.push(output);
		output = a;
		this.depth++;
	}

}
