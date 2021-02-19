package com.redislabs.lettusearch;

import com.redislabs.lettusearch.impl.protocol.CommandKeyword;
import com.redislabs.lettusearch.impl.protocol.CommandType;
import com.redislabs.lettusearch.impl.protocol.RediSearchCommandArgs;
import com.redislabs.lettusearch.output.*;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.internal.LettuceAssert;
import io.lettuce.core.output.*;
import io.lettuce.core.protocol.BaseRedisCommandBuilder;
import io.lettuce.core.protocol.Command;
import io.lettuce.core.protocol.CommandArgs;

import java.util.List;
import java.util.Map;

import static com.redislabs.lettusearch.impl.protocol.CommandKeyword.*;
import static com.redislabs.lettusearch.impl.protocol.CommandType.*;

/**
 * Dedicated pub/sub command builder to build pub/sub commands.
 */
public class RediSearchCommandBuilder<K, V> extends BaseRedisCommandBuilder<K, V> {

    static final String MUST_NOT_BE_NULL = "must not be null";

    public RediSearchCommandBuilder(RedisCodec<K, V> codec) {
        super(codec);
    }

    protected <A, B, T> Command<A, B, T> createCommand(CommandType type, CommandOutput<A, B, T> output, CommandArgs<A, B> args) {
        return new Command<>(type, output, args);
    }

    public Command<K, V, String> create(K index, CreateOptions<K, V> options, Field<K>... fields) {
        assertNotNull(index, "index");
        LettuceAssert.isTrue(fields.length > 0, "At least one field is required.");
        RediSearchCommandArgs<K, V> args = createArgs(index);
        if (options != null) {
            options.build(args);
        }
        args.add(CommandKeyword.SCHEMA);
        for (Field<K> field : fields) {
            field.build(args);
        }
        return createCommand(CREATE, new StatusOutput<>(codec), args);
    }

    public Command<K, V, String> dropIndex(K index, boolean deleteDocs) {
        assertNotNull(index, "index");
        RediSearchCommandArgs<K, V> args = createArgs(index);
        if (deleteDocs) {
            args.add(DD);
        }
        return createCommand(DROPINDEX, new StatusOutput<>(codec), args);
    }

    public Command<K, V, List<Object>> info(K index) {
        assertNotNull(index, "index");
        RediSearchCommandArgs<K, V> args = createArgs(index);
        return createCommand(INFO, new NestedMultiOutput<>(codec), args);
    }

    public Command<K, V, String> alter(K index, Field<K> field) {
        assertNotNull(index, "index");
        assertNotNull(field, "field");
        RediSearchCommandArgs<K, V> args = createArgs(index);
        args.add(SCHEMA);
        args.add(CommandKeyword.ADD);
        field.build(args);
        return createCommand(ALTER, new StatusOutput<>(codec), args);
    }

    private RediSearchCommandArgs<K, V> createArgs(K index) {
        return new RediSearchCommandArgs<>(codec).addKey(index);
    }

    public Command<K, V, SearchResults<K, V>> search(K index, V query, SearchOptions<K> options) {
        assertNotNull(index, "index");
        assertNotNull(query, "query");
        RediSearchCommandArgs<K, V> commandArgs = createArgs(index);
        commandArgs.addValue(query);
        if (options != null) {
            options.build(commandArgs);
        }
        return createCommand(SEARCH, searchOutput(options), commandArgs);
    }

    private CommandOutput<K, V, SearchResults<K, V>> searchOutput(SearchOptions<K> options) {
        if (options == null) {
            return new SearchOutput<>(codec);
        }
        if (options.isNoContent()) {
            return new SearchNoContentOutput<>(codec, options.isWithScores());
        }
        return new SearchOutput<>(codec, options.isWithScores(), options.isWithSortKeys(), options.isWithPayloads());
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

    public Command<K, V, AggregateWithCursorResults<K, V>> aggregate(K index, V query, Cursor cursor, AggregateOptions options) {
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
        args.add(CommandKeyword.DEL);
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
        return createCommand(SUGGET, suggestOutput(options), args);
    }

    private SuggestOutput<K, V> suggestOutput(SuggetOptions options) {
        if (options == null) {
            return new SuggestOutput<>(codec);
        }
        return new SuggestOutput<>(codec, options.isWithScores(), options.isWithPayloads());
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

    private void assertNotNull(Object arg, String name) {
        LettuceAssert.notNull(arg, name + " " + MUST_NOT_BE_NULL);
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

    public Command<K, V, List<K>> list() {
        return new Command<>(_LIST, new KeyListOutput<>(codec));
    }

}
