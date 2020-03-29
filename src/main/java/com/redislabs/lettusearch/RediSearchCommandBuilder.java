package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.protocol.CommandKeyword.COUNT;
import static com.redislabs.lettusearch.protocol.CommandKeyword.DD;
import static com.redislabs.lettusearch.protocol.CommandKeyword.INCR;
import static com.redislabs.lettusearch.protocol.CommandKeyword.PAYLOAD;
import static com.redislabs.lettusearch.protocol.CommandKeyword.READ;
import static com.redislabs.lettusearch.protocol.CommandKeyword.SCHEMA;
import static com.redislabs.lettusearch.protocol.CommandKeyword.WITHCURSOR;
import static com.redislabs.lettusearch.protocol.CommandKeyword.WITHSCORES;
import static com.redislabs.lettusearch.protocol.CommandKeyword.NOCONTENT;
import static com.redislabs.lettusearch.protocol.CommandType.ADD;
import static com.redislabs.lettusearch.protocol.CommandType.AGGREGATE;
import static com.redislabs.lettusearch.protocol.CommandType.ALIASADD;
import static com.redislabs.lettusearch.protocol.CommandType.ALIASDEL;
import static com.redislabs.lettusearch.protocol.CommandType.ALIASUPDATE;
import static com.redislabs.lettusearch.protocol.CommandType.ALTER;
import static com.redislabs.lettusearch.protocol.CommandType.CREATE;
import static com.redislabs.lettusearch.protocol.CommandType.CURSOR;
import static com.redislabs.lettusearch.protocol.CommandType.DEL;
import static com.redislabs.lettusearch.protocol.CommandType.DROP;
import static com.redislabs.lettusearch.protocol.CommandType.GET;
import static com.redislabs.lettusearch.protocol.CommandType.INFO;
import static com.redislabs.lettusearch.protocol.CommandType.MGET;
import static com.redislabs.lettusearch.protocol.CommandType.SEARCH;
import static com.redislabs.lettusearch.protocol.CommandType.SUGADD;
import static com.redislabs.lettusearch.protocol.CommandType.SUGDEL;
import static com.redislabs.lettusearch.protocol.CommandType.SUGGET;
import static com.redislabs.lettusearch.protocol.CommandType.SUGLEN;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateResults;
import com.redislabs.lettusearch.aggregate.AggregateWithCursorResults;
import com.redislabs.lettusearch.aggregate.Cursor;
import com.redislabs.lettusearch.index.CreateOptions;
import com.redislabs.lettusearch.index.DropOptions;
import com.redislabs.lettusearch.index.Schema;
import com.redislabs.lettusearch.index.field.FieldOptions;
import com.redislabs.lettusearch.output.AggregateOutput;
import com.redislabs.lettusearch.output.AggregateWithCursorOutput;
import com.redislabs.lettusearch.output.MapListOutput;
import com.redislabs.lettusearch.output.SearchNoContentOutput;
import com.redislabs.lettusearch.output.SearchOutput;
import com.redislabs.lettusearch.output.SuggestOutput;
import com.redislabs.lettusearch.protocol.CommandKeyword;
import com.redislabs.lettusearch.protocol.CommandType;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;
import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.suggest.SuggetOptions;
import com.redislabs.lettusearch.suggest.SuggetResult;

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

	public Command<K, V, String> add(String index, K docId, double score, Map<K, V> fields, V payload,
			AddOptions options) {
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

	public Command<K, V, String> create(String index, Schema schema, CreateOptions options) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		LettuceAssert.notEmpty(index, "index " + MUST_NOT_BE_EMPTY);
		LettuceAssert.notNull(schema, "schema " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = createArgs(index);
		if (options != null) {
			options.build(args);
		}
		schema.build(args);
		return createCommand(CREATE, new StatusOutput<>(codec), args);
	}

	public Command<K, V, String> drop(String index, DropOptions options) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = createArgs(index);
		if (options != null) {
			options.build(args);
		}
		return createCommand(DROP, new StatusOutput<>(codec), args);
	}

	public Command<K, V, List<Object>> info(String index) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = createArgs(index);
		return createCommand(INFO, new NestedMultiOutput<>(codec), args);
	}

	public Command<K, V, String> alter(String index, K field, FieldOptions options) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = createArgs(index);
		args.add(SCHEMA);
		args.add(com.redislabs.lettusearch.protocol.CommandKeyword.ADD);
		args.addKey(field);
		if (options != null) {
			options.build(args);
		}
		return createCommand(ALTER, new StatusOutput<>(codec), args);
	}

	private RediSearchCommandArgs<K, V> createArgs(String index) {
		return new RediSearchCommandArgs<>(codec).add(index);
	}

	public Command<K, V, SearchResults<K, V>> search(String index, String query, SearchOptions options) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		LettuceAssert.notNull(query, "query " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> commandArgs = createArgs(index);
		commandArgs.add(query);
		if (options != null) {
			options.build(commandArgs);
		}
		return createCommand(SEARCH, getSearchOutput(codec, options != null && options.isNoContent(),
				options != null && options.isWithScores()), commandArgs);
	}

	public Command<K, V, SearchResults<K, V>> search(String index, String query, Object... options) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		LettuceAssert.notNull(query, "query " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> commandArgs = createArgs(index);
		commandArgs.add(query);
		boolean noContent = false;
		boolean withScores = false;
		for (Object option : options) {
			String optionString = String.valueOf(option);
			if (WITHSCORES.name().equalsIgnoreCase(optionString)) {
				withScores = true;
			}
			if (NOCONTENT.name().equalsIgnoreCase(optionString)) {
				noContent = true;
			}
			commandArgs.add(optionString);
		}
		return createCommand(SEARCH, getSearchOutput(codec, noContent, withScores), commandArgs);
	}

	private CommandOutput<K, V, SearchResults<K, V>> getSearchOutput(RedisCodec<K, V> codec, boolean noContent,
			boolean withScores) {
		if (noContent) {
			return new SearchNoContentOutput<>(codec, withScores);
		}
		return new SearchOutput<>(codec, withScores);
	}

	public Command<K, V, AggregateResults<K, V>> aggregate(String index, String query, AggregateOptions options) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		LettuceAssert.notNull(query, "query " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = createArgs(index);
		args.add(query);
		if (options != null) {
			options.build(args);
		}
		return createCommand(AGGREGATE, new AggregateOutput<>(codec, new AggregateResults<>()), args);
	}

	public Command<K, V, AggregateResults<K, V>> aggregate(String index, String query, Object... options) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		LettuceAssert.notNull(query, "query " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = createArgs(index);
		args.add(query);
		for (Object option : options) {
			args.add(String.valueOf(option));
		}
		return createCommand(AGGREGATE, new AggregateOutput<>(codec, new AggregateResults<>()), args);
	}

	public Command<K, V, AggregateWithCursorResults<K, V>> aggregate(String index, String query, Cursor cursor,
			AggregateOptions options) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		LettuceAssert.notNull(query, "query " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = createArgs(index);
		args.add(query);
		if (options != null) {
			options.build(args);
		}
		args.add(WITHCURSOR);
		if (cursor != null) {
			cursor.build(args);
		}
		return createCommand(AGGREGATE, new AggregateWithCursorOutput<>(codec), args);
	}

	public Command<K, V, AggregateWithCursorResults<K, V>> aggregate(String index, String query, Cursor cursor,
			Object... options) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		LettuceAssert.notNull(query, "query " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = createArgs(index);
		args.add(query);
		for (Object option : options) {
			args.add(String.valueOf(option));
		}
		args.add(WITHCURSOR);
		if (cursor != null) {
			cursor.build(args);
		}
		return createCommand(AGGREGATE, new AggregateWithCursorOutput<>(codec), args);
	}

	public Command<K, V, AggregateWithCursorResults<K, V>> cursorRead(String index, long cursor, Long count) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = new RediSearchCommandArgs<>(codec);
		args.add(READ);
		args.add(index);
		args.add(cursor);
		if (count != null) {
			args.add(COUNT);
			args.add(count);
		}
		return createCommand(CURSOR, new AggregateWithCursorOutput<>(codec), args);
	}

	public Command<K, V, String> cursorDelete(String index, long cursor) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = new RediSearchCommandArgs<>(codec);
		args.add(com.redislabs.lettusearch.protocol.CommandKeyword.DEL);
		args.add(index);
		args.add(cursor);
		return createCommand(CURSOR, new StatusOutput<>(codec), args);
	}

	public Command<K, V, Long> sugadd(K key, V string, double score, boolean increment, V payload) {
		LettuceAssert.notNull(key, "key " + MUST_NOT_BE_NULL);
		LettuceAssert.notNull(string, "string " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = new RediSearchCommandArgs<>(codec);
		args.addKey(key);
		args.addValue(string);
		args.add(score);
		if (increment) {
			args.add(INCR);
		}
		if (payload != null) {
			args.add(PAYLOAD);
			args.addValue(payload);
		}
		return createCommand(SUGADD, new IntegerOutput<>(codec), args);
	}

	public Command<K, V, List<SuggetResult<V>>> sugget(K key, V prefix, SuggetOptions options) {
		LettuceAssert.notNull(key, "key " + MUST_NOT_BE_NULL);
		LettuceAssert.notNull(prefix, "prefix " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = new RediSearchCommandArgs<>(codec);
		args.addKey(key);
		args.addValue(prefix);
		if (options != null) {
			options.build(args);
		}
		return createCommand(SUGGET, new SuggestOutput<>(codec, options), args);
	}

	public Command<K, V, Boolean> sugdel(K key, V string) {
		LettuceAssert.notNull(key, "key " + MUST_NOT_BE_NULL);
		LettuceAssert.notNull(string, "string " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = new RediSearchCommandArgs<>(codec).addKey(key).addValue(string);
		return createCommand(SUGDEL, new BooleanOutput<>(codec), args);
	}

	public Command<K, V, Long> suglen(K key) {
		LettuceAssert.notNull(key, "key " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = new RediSearchCommandArgs<>(codec).addKey(key);
		return createCommand(SUGLEN, new IntegerOutput<>(codec), args);

	}

	public Command<K, V, Map<K, V>> get(String index, K docId) {
		LettuceAssert.notNull(docId, "docId " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = createArgs(index);
		args.addKey(docId);
		return createCommand(GET, new MapOutput<K, V>(codec), args);
	}

	public Command<K, V, List<Map<K, V>>> mget(String index, @SuppressWarnings("unchecked") K... docIds) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		LettuceAssert.notNull(docIds, "docId " + MUST_NOT_BE_EMPTY);
		RediSearchCommandArgs<K, V> args = createArgs(index);
		args.addKeys(docIds);
		return createCommand(MGET, new MapListOutput<>(codec), args);
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

	public Command<K, V, String> aliasAdd(String name, String index) {
		LettuceAssert.notNull(name, "name " + MUST_NOT_BE_NULL);
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = new RediSearchCommandArgs<>(codec);
		args.add(name);
		args.add(index);
		return createCommand(ALIASADD, new StatusOutput<>(codec), args);
	}

	public Command<K, V, String> aliasUpdate(String name, String index) {
		LettuceAssert.notNull(name, "name " + MUST_NOT_BE_NULL);
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = new RediSearchCommandArgs<>(codec);
		args.add(name);
		args.add(index);
		return createCommand(ALIASUPDATE, new StatusOutput<>(codec), args);
	}

	public Command<K, V, String> aliasDel(String name) {
		LettuceAssert.notNull(name, "name " + MUST_NOT_BE_NULL);
		RediSearchCommandArgs<K, V> args = new RediSearchCommandArgs<>(codec);
		args.add(name);
		return createCommand(ALIASDEL, new StatusOutput<>(codec), args);
	}

}
