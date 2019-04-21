package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.CommandKeyword.DD;
import static com.redislabs.lettusearch.CommandKeyword.INCR;
import static com.redislabs.lettusearch.CommandKeyword.PAYLOAD;
import static com.redislabs.lettusearch.CommandKeyword.SCHEMA;
import static com.redislabs.lettusearch.CommandType.ADD;
import static com.redislabs.lettusearch.CommandType.AGGREGATE;
import static com.redislabs.lettusearch.CommandType.ALTER;
import static com.redislabs.lettusearch.CommandType.CREATE;
import static com.redislabs.lettusearch.CommandType.DEL;
import static com.redislabs.lettusearch.CommandType.DROP;
import static com.redislabs.lettusearch.CommandType.GET;
import static com.redislabs.lettusearch.CommandType.INFO;
import static com.redislabs.lettusearch.CommandType.SEARCH;
import static com.redislabs.lettusearch.CommandType.SUGADD;
import static com.redislabs.lettusearch.CommandType.SUGGET;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateOutput;
import com.redislabs.lettusearch.aggregate.AggregateResults;
import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.DropOptions;
import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.SearchNoContentOutput;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchOutput;
import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.search.field.FieldOptions;
import com.redislabs.lettusearch.suggest.SuggestGetOptions;
import com.redislabs.lettusearch.suggest.SuggestOutput;
import com.redislabs.lettusearch.suggest.SuggestResult;

import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.internal.LettuceAssert;
import io.lettuce.core.output.BooleanOutput;
import io.lettuce.core.output.CommandOutput;
import io.lettuce.core.output.IntegerOutput;
import io.lettuce.core.output.MapOutput;
import io.lettuce.core.output.NestedMultiOutput;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.BaseRedisCommandBuilder;
import io.lettuce.core.protocol.Command;
import io.lettuce.core.protocol.CommandArgs;

/**
 * Dedicated pub/sub command builder to build pub/sub commands.
 *
 */
public class RediSearchCommandBuilder<K, V> extends BaseRedisCommandBuilder<K, V> {

	static final String MUST_NOT_BE_NULL = "must not be null";
	static final String MUST_NOT_BE_EMPTY = "must not be empty";

	public RediSearchCommandBuilder(RedisCodec<K, V> codec) {
		super(codec);
	}

	protected <A, B, T> Command<A, B, T> createCommand(CommandType type, CommandOutput<A, B, T> output,
			CommandArgs<A, B> args) {
		return new Command<A, B, T>(type, output, args);
	}

	public Command<K, V, String> add(String index, K docId, double score, Map<K, V> fields, AddOptions options,
			V payload) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		LettuceAssert.notNull(docId, "docId " + MUST_NOT_BE_NULL);
		LettuceAssert.notNull(fields, "fields " + MUST_NOT_BE_NULL);
		LettuceAssert.isTrue(!fields.isEmpty(), "fields " + MUST_NOT_BE_EMPTY);
		RediSearchCommandArgs<K, V> args = createArgs(index);
		args.addKey(docId);
		args.add(score);
		if (options != null) {
			options.build(args);
		}
		if (payload != null) {
			args.add(PAYLOAD);
			args.addValue(payload);
		}
		args.add(CommandKeyword.FIELDS);
		for (Entry<K, V> entry : fields.entrySet()) {
			args.addKey(entry.getKey());
			args.addValue(entry.getValue());
		}
		return createCommand(ADD, new StatusOutput<>(codec), args);
	}

	public Command<K, V, String> create(String index, Schema schema) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		LettuceAssert.notEmpty(index, "index " + MUST_NOT_BE_EMPTY);
		LettuceAssert.notNull(schema, "schema " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = createArgs(index);
		schema.build(args);
		return createCommand(CREATE, new StatusOutput<>(codec), args);
	}

	public Command<K, V, String> drop(String index, DropOptions options) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = createArgs(index);
		options.build(args);
		return createCommand(DROP, new StatusOutput<>(codec), args);
	}

	public Command<K, V, List<Object>> indexInfo(String index) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = createArgs(index);
		return createCommand(INFO, new NestedMultiOutput<>(codec), args);
	}

	public Command<K, V, String> alter(String index, K field, FieldOptions options) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = createArgs(index);
		args.add(SCHEMA);
		args.add(com.redislabs.lettusearch.CommandKeyword.ADD);
		args.addKey(field);
		options.build(args);
		return createCommand(ALTER, new StatusOutput<>(codec), args);
	}

	private RediSearchCommandArgs<K, V> createArgs(String index) {
		return new RediSearchCommandArgs<>(codec).add(index);
	}

	public Command<K, V, SearchResults<K, V>> search(String index, String query, SearchOptions options) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		LettuceAssert.notNull(query, "query " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = createArgs(index);
		args.add(query);
		options.build(args);
		return createCommand(SEARCH, getSearchOutput(codec, options), args);
	}

	private CommandOutput<K, V, SearchResults<K, V>> getSearchOutput(RedisCodec<K, V> codec, SearchOptions options) {
		if (options.isNoContent()) {
			return new SearchNoContentOutput<>(codec, options);
		}
		return new SearchOutput<>(codec, options);
	}

	public Command<K, V, AggregateResults<K, V>> aggregate(String index, String query, AggregateOptions options) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		LettuceAssert.notNull(query, "query " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = createArgs(index);
		args.add(query);
		options.build(args);
		return createCommand(AGGREGATE, new AggregateOutput<>(codec), args);
	}

	public Command<K, V, Long> sugadd(K key, V string, double score, boolean increment, V payload) {
		LettuceAssert.notNull(key, "key " + MUST_NOT_BE_NULL);
		LettuceAssert.notNull(string, "string " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = new RediSearchCommandArgs<>(codec).addKey(key).addValue(string).add(score);
		if (increment) {
			args.add(INCR);
		}
		if (payload != null) {
			args.add(PAYLOAD);
			args.addValue(payload);
		}
		return createCommand(SUGADD, new IntegerOutput<>(codec), args);
	}

	public Command<K, V, List<SuggestResult<V>>> sugget(K key, V prefix, SuggestGetOptions options) {
		LettuceAssert.notNull(key, "key " + MUST_NOT_BE_NULL);
		LettuceAssert.notNull(prefix, "prefix " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = new RediSearchCommandArgs<>(codec).addKey(key).addValue(prefix);
		options.build(args);
		return createCommand(SUGGET, new SuggestOutput<>(codec, options), args);
	}

	public Command<K, V, Map<K, V>> get(String index, K docId) {
		LettuceAssert.notNull(docId, "docId " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = createArgs(index);
		args.addKey(docId);
		return createCommand(GET, new MapOutput<K, V>(codec), args);
	}

	public Command<K, V, Boolean> del(String index, K docId, boolean deleteDoc) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		LettuceAssert.notNull(docId, "docId " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = createArgs(index);
		args.addKey(docId);
		if (deleteDoc) {
			args.add(DD);
		}
		return createCommand(DEL, new BooleanOutput<>(codec), args);
	}
}
