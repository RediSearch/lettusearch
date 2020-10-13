package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.protocol.CommandKeyword.COUNT;
import static com.redislabs.lettusearch.protocol.CommandKeyword.DD;
import static com.redislabs.lettusearch.protocol.CommandKeyword.INCR;
import static com.redislabs.lettusearch.protocol.CommandKeyword.NOCONTENT;
import static com.redislabs.lettusearch.protocol.CommandKeyword.PAYLOAD;
import static com.redislabs.lettusearch.protocol.CommandKeyword.READ;
import static com.redislabs.lettusearch.protocol.CommandKeyword.SCHEMA;
import static com.redislabs.lettusearch.protocol.CommandKeyword.WITHCURSOR;
import static com.redislabs.lettusearch.protocol.CommandKeyword.WITHPAYLOADS;
import static com.redislabs.lettusearch.protocol.CommandKeyword.WITHSCORES;
import static com.redislabs.lettusearch.protocol.CommandKeyword.WITHSORTKEYS;
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
import com.redislabs.lettusearch.output.ExtendedNestedMultiOutput;
import com.redislabs.lettusearch.output.MapListOutput;
import com.redislabs.lettusearch.output.SearchNoContentOutput;
import com.redislabs.lettusearch.output.SearchOutput;
import com.redislabs.lettusearch.output.SuggestOutput;
import com.redislabs.lettusearch.protocol.CommandKeyword;
import com.redislabs.lettusearch.protocol.CommandType;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;
import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.suggest.Suggestion;
import com.redislabs.lettusearch.suggest.SuggetOptions;

import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.internal.LettuceAssert;
import io.lettuce.core.output.BooleanOutput;
import io.lettuce.core.output.CommandOutput;
import io.lettuce.core.output.IntegerOutput;
import io.lettuce.core.output.MapOutput;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.BaseRedisCommandBuilder;
import io.lettuce.core.protocol.Command;
import io.lettuce.core.protocol.CommandArgs;

/**
 * Dedicated pub/sub command builder to build pub/sub commands.
 */
public class RediSearchCommandBuilder<K, V> extends BaseRedisCommandBuilder<K, V> {

	static final String MUST_NOT_BE_NULL = "must not be null";
	static final String MUST_NOT_BE_EMPTY = "must not be empty";

	public RediSearchCommandBuilder(RedisCodec<K, V> codec) {
		super(codec);
	}

	protected <A, B, T> Command<A, B, T> createCommand(CommandType type, CommandOutput<A, B, T> output,
			CommandArgs<A, B> args) {
		return new Command<>(type, output, args);
	}

	public Command<K, V, String> add(K index, Document<K, V> document) {
		return add(index, document, null);
	}

	public Command<K, V, String> add(K index, Document<K, V> document, AddOptions options) {
		assertNotNull(index, "index");
		assertNotNull(document, "document");
		assertNotNull(document.getId(), "document id");
		assertNotNull(document.getScore(), "document score");
		LettuceAssert.isTrue(!document.isEmpty(), "document fields " + MUST_NOT_BE_EMPTY);
		RediSearchCommandArgs<K, V> args = createArgs(index);
		args.addKey(document.getId());
		args.add(document.getScore());
		if (options != null) {
			options.build(args);
		}
		if (document.getPayload() != null) {
			args.add(PAYLOAD);
			args.addValue(document.getPayload());
		}
		args.add(CommandKeyword.FIELDS);
		for (Entry<K, V> entry : document.entrySet()) {
			args.addKey(entry.getKey());
			args.addValue(entry.getValue());
		}
		return createCommand(ADD, new StatusOutput<>(codec), args);
	}

	public Command<K, V, String> create(K index, Schema<K> schema) {
		return create(index, schema, null);
	}

	public Command<K, V, String> create(K index, Schema<K> schema, CreateOptions<K, V> options) {
		assertNotNull(index, "index");
		assertNotNull(schema, "schema");
		RediSearchCommandArgs<K, V> args = createArgs(index);
		if (options != null) {
			options.build(args);
		}
		schema.build(args);
		return createCommand(CREATE, new StatusOutput<>(codec), args);
	}

	public Command<K, V, String> drop(K index) {
		return drop(index, null);
	}

	public Command<K, V, String> drop(K index, DropOptions options) {
		assertNotNull(index, "index");
		RediSearchCommandArgs<K, V> args = createArgs(index);
		if (options != null) {
			options.build(args);
		}
		return createCommand(DROP, new StatusOutput<>(codec), args);
	}

	public Command<K, V, List<Object>> info(K index) {
		assertNotNull(index, "index");
		RediSearchCommandArgs<K, V> args = createArgs(index);
		return createCommand(INFO, new ExtendedNestedMultiOutput<>(codec), args);
	}

	public Command<K, V, String> alter(K index, K field, FieldOptions options) {
		assertNotNull(index, "index");
		RediSearchCommandArgs<K, V> args = createArgs(index);
		args.add(SCHEMA);
		args.add(com.redislabs.lettusearch.protocol.CommandKeyword.ADD);
		args.addKey(field);
		if (options != null) {
			options.build(args);
		}
		return createCommand(ALTER, new StatusOutput<>(codec), args);
	}

	private RediSearchCommandArgs<K, V> createArgs(K index) {
		return new RediSearchCommandArgs<>(codec).addKey(index);
	}

	public Command<K, V, SearchResults<K, V>> search(K index, V query) {
		return search(index, query, (SearchOptions<K>) null);
	}

	public Command<K, V, SearchResults<K, V>> search(K index, V query, SearchOptions<K> options) {
		assertNotNull(index, "index");
		assertNotNull(query, "query");
		RediSearchCommandArgs<K, V> commandArgs = createArgs(index);
		commandArgs.addValue(query);
		if (options != null) {
			options.build(commandArgs);
		}
		boolean withScores = false;
		boolean withSortKeys = false;
		boolean withPayloads = false;
		boolean noContent = false;
		if (options != null) {
			withScores = options.isWithScores();
			withSortKeys = options.isWithSortKeys();
			withPayloads = options.isWithPayloads();
			noContent = options.isNoContent();
		}
		return createCommand(SEARCH, getSearchOutput(codec, noContent, withScores, withSortKeys, withPayloads),
				commandArgs);
	}

	public Command<K, V, SearchResults<K, V>> search(K index, V query, Object... options) {
		assertNotNull(index, "index");
		assertNotNull(query, "query");
		RediSearchCommandArgs<K, V> commandArgs = createArgs(index);
		commandArgs.addValue(query);
		boolean noContent = false;
		boolean withScores = false;
		boolean withSortKeys = false;
		boolean withPayloads = false;
		for (Object option : options) {
			String optionString = String.valueOf(option);
			if (WITHSCORES.name().equalsIgnoreCase(optionString)) {
				withScores = true;
			}
			if (WITHSORTKEYS.name().equalsIgnoreCase(optionString)) {
				withSortKeys = true;
			}
			if (WITHPAYLOADS.name().equalsIgnoreCase(optionString)) {
				withPayloads = true;
			}
			if (NOCONTENT.name().equalsIgnoreCase(optionString)) {
				noContent = true;
			}
			commandArgs.add(optionString);
		}
		return createCommand(SEARCH, getSearchOutput(codec, noContent, withScores, withSortKeys, withPayloads),
				commandArgs);
	}

	private CommandOutput<K, V, SearchResults<K, V>> getSearchOutput(RedisCodec<K, V> codec, boolean noContent,
			boolean withScores, boolean withSortKeys, boolean withPayloads) {
		if (noContent) {
			return new SearchNoContentOutput<>(codec, withScores);
		}
		return new SearchOutput<>(codec, withScores, withSortKeys, withPayloads);
	}

	public Command<K, V, AggregateResults<K, V>> aggregate(K index, V query) {
		return aggregate(index, query, (AggregateOptions) null);
	}

	public Command<K, V, AggregateResults<K, V>> aggregate(K index, V query, AggregateOptions options) {
		assertNotNull(index, "index");
		assertNotNull(query, "query");
		RediSearchCommandArgs<K, V> args = createArgs(index);
		args.addValue(query);
		if (options != null) {
			options.build(args);
		}
		return createCommand(AGGREGATE, new AggregateOutput<>(codec, new AggregateResults<>()), args);
	}

	public Command<K, V, AggregateResults<K, V>> aggregate(K index, V query, Object... options) {
		assertNotNull(index, "index");
		assertNotNull(query, "query");
		RediSearchCommandArgs<K, V> args = createArgs(index);
		args.addValue(query);
		for (Object option : options) {
			args.add(String.valueOf(option));
		}
		return createCommand(AGGREGATE, new AggregateOutput<>(codec, new AggregateResults<>()), args);
	}

	public Command<K, V, AggregateWithCursorResults<K, V>> aggregate(K index, V query, Cursor cursor,
			AggregateOptions options) {
		assertNotNull(index, "index");
		assertNotNull(query, "query");
		RediSearchCommandArgs<K, V> args = createArgs(index);
		args.addValue(query);
		if (options != null) {
			options.build(args);
		}
		args.add(WITHCURSOR);
		if (cursor != null) {
			cursor.build(args);
		}
		return createCommand(AGGREGATE, new AggregateWithCursorOutput<>(codec), args);
	}

	public Command<K, V, AggregateWithCursorResults<K, V>> aggregate(K index, V query, Cursor cursor,
			Object... options) {
		assertNotNull(index, "index");
		assertNotNull(query, "query");
		RediSearchCommandArgs<K, V> args = createArgs(index);
		args.addValue(query);
		for (Object option : options) {
			args.add(String.valueOf(option));
		}
		args.add(WITHCURSOR);
		if (cursor != null) {
			cursor.build(args);
		}
		return createCommand(AGGREGATE, new AggregateWithCursorOutput<>(codec), args);
	}

	public Command<K, V, AggregateWithCursorResults<K, V>> cursorRead(K index, long cursor) {
		return cursorRead(index, cursor, null);
	}

	public Command<K, V, AggregateWithCursorResults<K, V>> cursorRead(K index, long cursor, Long count) {
		assertNotNull(index, "index");
		RediSearchCommandArgs<K, V> args = new RediSearchCommandArgs<>(codec);
		args.add(READ);
		args.addKey(index);
		args.add(cursor);
		if (count != null) {
			args.add(COUNT);
			args.add(count);
		}
		return createCommand(CURSOR, new AggregateWithCursorOutput<>(codec), args);
	}

	public Command<K, V, String> cursorDelete(K index, long cursor) {
		assertNotNull(index, "index");
		RediSearchCommandArgs<K, V> args = new RediSearchCommandArgs<>(codec);
		args.add(com.redislabs.lettusearch.protocol.CommandKeyword.DEL);
		args.addKey(index);
		args.add(cursor);
		return createCommand(CURSOR, new StatusOutput<>(codec), args);
	}

	public Command<K, V, Long> sugadd(K key, Suggestion<V> suggestion) {
		return sugadd(key, suggestion, false);
	}

	public Command<K, V, Long> sugadd(K key, Suggestion<V> suggestion, boolean increment) {
		assertNotNull(key, "key");
		assertNotNull(suggestion.getString(), "suggestion string");
		assertNotNull(suggestion.getScore(), "suggestion score");
		RediSearchCommandArgs<K, V> args = new RediSearchCommandArgs<>(codec);
		args.addKey(key);
		args.addValue(suggestion.getString());
		args.add(suggestion.getScore());
		if (increment) {
			args.add(INCR);
		}
		if (suggestion.getPayload() != null) {
			args.add(PAYLOAD);
			args.addValue(suggestion.getPayload());
		}
		return createCommand(SUGADD, new IntegerOutput<>(codec), args);
	}

	public Command<K, V, List<Suggestion<V>>> sugget(K key, V prefix) {
		return sugget(key, prefix, null);
	}

	public Command<K, V, List<Suggestion<V>>> sugget(K key, V prefix, SuggetOptions options) {
		assertNotNull(key, "key");
		assertNotNull(prefix, "prefix");
		RediSearchCommandArgs<K, V> args = new RediSearchCommandArgs<>(codec);
		args.addKey(key);
		args.addValue(prefix);
		if (options != null) {
			options.build(args);
		}
		return createCommand(SUGGET, new SuggestOutput<>(codec, options), args);
	}

	public Command<K, V, Boolean> sugdel(K key, V string) {
		assertNotNull(key, "key");
		assertNotNull(string, "string");
		RediSearchCommandArgs<K, V> args = new RediSearchCommandArgs<>(codec).addKey(key).addValue(string);
		return createCommand(SUGDEL, new BooleanOutput<>(codec), args);
	}

	public Command<K, V, Long> suglen(K key) {
		assertNotNull(key, "key");
		RediSearchCommandArgs<K, V> args = new RediSearchCommandArgs<>(codec).addKey(key);
		return createCommand(SUGLEN, new IntegerOutput<>(codec), args);

	}

	public Command<K, V, Map<K, V>> get(K index, K docId) {
		assertNotNull(docId, "docId");
		RediSearchCommandArgs<K, V> args = createArgs(index);
		args.addKey(docId);
		return createCommand(GET, new MapOutput<>(codec), args);
	}

	@SuppressWarnings("unchecked")
	public Command<K, V, List<Map<K, V>>> mget(K index, K... docIds) {
		assertNotNull(index, "index");
		LettuceAssert.notEmpty(docIds, "docId " + MUST_NOT_BE_EMPTY);
		RediSearchCommandArgs<K, V> args = createArgs(index);
		args.addKeys(docIds);
		return createCommand(MGET, new MapListOutput<>(codec), args);
	}

	private void assertNotNull(Object arg, String name) {
		LettuceAssert.notNull(arg, name + " " + MUST_NOT_BE_NULL);
	}

	public Command<K, V, Boolean> del(K index, K docId) {
		return del(index, docId, false);
	}

	public Command<K, V, Boolean> del(K index, K docId, boolean deleteDoc) {
		assertNotNull(index, "index");
		assertNotNull(docId, "docId");
		RediSearchCommandArgs<K, V> args = createArgs(index);
		args.addKey(docId);
		if (deleteDoc) {
			args.add(DD);
		}
		return createCommand(DEL, new BooleanOutput<>(codec), args);
	}

	public Command<K, V, String> aliasAdd(K name, K index) {
		assertNotNull(name, "name");
		assertNotNull(index, "index");
		RediSearchCommandArgs<K, V> args = new RediSearchCommandArgs<>(codec);
		args.addKey(name);
		args.addKey(index);
		return createCommand(ALIASADD, new StatusOutput<>(codec), args);
	}

	public Command<K, V, String> aliasUpdate(K name, K index) {
		assertNotNull(name, "name");
		assertNotNull(index, "index");
		RediSearchCommandArgs<K, V> args = new RediSearchCommandArgs<>(codec);
		args.addKey(name);
		args.addKey(index);
		return createCommand(ALIASUPDATE, new StatusOutput<>(codec), args);
	}

	public Command<K, V, String> aliasDel(K name) {
		assertNotNull(name, "name");
		RediSearchCommandArgs<K, V> args = new RediSearchCommandArgs<>(codec);
		args.addKey(name);
		return createCommand(ALIASDEL, new StatusOutput<>(codec), args);
	}

}
