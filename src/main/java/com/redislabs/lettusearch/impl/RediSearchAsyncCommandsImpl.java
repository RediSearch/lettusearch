package com.redislabs.lettusearch.impl;

import java.util.List;
import java.util.Map;

import com.redislabs.lettusearch.RediSearchAsyncCommands;
import com.redislabs.lettusearch.RediSearchCommandBuilder;
import com.redislabs.lettusearch.StatefulRediSearchConnection;
import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateResults;
import com.redislabs.lettusearch.aggregate.AggregateWithCursorResults;
import com.redislabs.lettusearch.aggregate.Cursor;
import com.redislabs.lettusearch.index.CreateOptions;
import com.redislabs.lettusearch.index.DropOptions;
import com.redislabs.lettusearch.index.Schema;
import com.redislabs.lettusearch.index.field.FieldOptions;
import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.suggest.Suggestion;
import com.redislabs.lettusearch.suggest.SuggetOptions;

import io.lettuce.core.RedisAsyncCommandsImpl;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.codec.RedisCodec;

public class RediSearchAsyncCommandsImpl<K, V> extends RedisAsyncCommandsImpl<K, V> implements RediSearchAsyncCommands<K, V> {

    private final StatefulRediSearchConnection<K, V> connection;
    private final RediSearchCommandBuilder<K, V> commandBuilder;

    public RediSearchAsyncCommandsImpl(StatefulRediSearchConnection<K, V> connection, RedisCodec<K, V> codec) {
        super(connection, codec);
        this.connection = connection;
        this.commandBuilder = new RediSearchCommandBuilder<>(codec);
    }

    @Override
    public StatefulRediSearchConnection<K, V> getStatefulConnection() {
        return connection;
    }

    @Override
    public RedisFuture<String> add(K index, Document<K, V> document) {
        return dispatch(commandBuilder.add(index, document));
    }

    @Override
    public RedisFuture<String> add(K index, Document<K, V> document, AddOptions options) {
        return dispatch(commandBuilder.add(index, document, options));
    }

    @Override
    public RedisFuture<String> create(K index, Schema schema) {
        return dispatch(commandBuilder.create(index, schema));
    }

    @Override
    public RedisFuture<String> create(K index, Schema schema, CreateOptions options) {
        return dispatch(commandBuilder.create(index, schema, options));
    }

    @Override
    public RedisFuture<String> drop(K index) {
        return dispatch(commandBuilder.drop(index));
    }


    @Override
    public RedisFuture<String> drop(K index, DropOptions options) {
        return dispatch(commandBuilder.drop(index, options));
    }

    @Override
    public RedisFuture<List<Object>> ftInfo(K index) {
        return dispatch(commandBuilder.info(index));
    }

    @Override
    public RedisFuture<SearchResults<K, V>> search(K index, V query) {
        return dispatch(commandBuilder.search(index, query));
    }

    @Override
    public RedisFuture<SearchResults<K, V>> search(K index, V query, SearchOptions options) {
        return dispatch(commandBuilder.search(index, query, options));
    }

    @Override
    public RedisFuture<SearchResults<K, V>> search(K index, V query, Object... options) {
        return dispatch(commandBuilder.search(index, query, options));
    }

    @Override
    public RedisFuture<AggregateResults<K, V>> aggregate(K index, V query) {
        return dispatch(commandBuilder.aggregate(index, query));
    }

    @Override
    public RedisFuture<AggregateResults<K, V>> aggregate(K index, V query, AggregateOptions options) {
        return dispatch(commandBuilder.aggregate(index, query, options));
    }

    @Override
    public RedisFuture<AggregateResults<K, V>> aggregate(K index, V query, Object... options) {
        return dispatch(commandBuilder.aggregate(index, query, options));
    }

    @Override
    public RedisFuture<AggregateWithCursorResults<K, V>> aggregate(K index, V query, Cursor cursor) {
        return dispatch(commandBuilder.aggregate(index, query, cursor));
    }

    @Override
    public RedisFuture<AggregateWithCursorResults<K, V>> aggregate(K index, V query, Cursor cursor, AggregateOptions options) {
        return dispatch(commandBuilder.aggregate(index, query, cursor, options));
    }

    @Override
    public RedisFuture<AggregateWithCursorResults<K, V>> aggregate(K index, V query, Cursor cursor, Object... options) {
        return dispatch(commandBuilder.aggregate(index, query, cursor, options));
    }

    @Override
    public RedisFuture<AggregateWithCursorResults<K, V>> cursorRead(K index, long cursor) {
        return dispatch(commandBuilder.cursorRead(index, cursor));
    }

    @Override
    public RedisFuture<AggregateWithCursorResults<K, V>> cursorRead(K index, long cursor, long count) {
        return dispatch(commandBuilder.cursorRead(index, cursor, count));
    }

    @Override
    public RedisFuture<AggregateWithCursorResults<K, V>> cursorRead(K index, long cursor, Long count) {
        return dispatch(commandBuilder.cursorRead(index, cursor, count));
    }

    @Override
    public RedisFuture<String> cursorDelete(K index, long cursor) {
        return dispatch(commandBuilder.cursorDelete(index, cursor));
    }

    @Override
    public RedisFuture<Long> sugadd(K key, Suggestion<V> suggestion) {
        return dispatch(commandBuilder.sugadd(key, suggestion));
    }

    @Override
    public RedisFuture<Long> sugadd(K key, Suggestion<V> suggestion, boolean increment) {
        return dispatch(commandBuilder.sugadd(key, suggestion, increment));
    }

    @Override
    public RedisFuture<List<Suggestion<V>>> sugget(K key, V prefix) {
        return dispatch(commandBuilder.sugget(key, prefix));
    }

    @Override
    public RedisFuture<List<Suggestion<V>>> sugget(K key, V prefix, SuggetOptions options) {
        return dispatch(commandBuilder.sugget(key, prefix, options));
    }

    @Override
    public RedisFuture<Boolean> sugdel(K key, V string) {
        return dispatch(commandBuilder.sugdel(key, string));
    }

    @Override
    public RedisFuture<Long> suglen(K key) {
        return dispatch(commandBuilder.suglen(key));
    }

    @Override
    public RedisFuture<Map<K, V>> get(K index, K docId) {
        return dispatch(commandBuilder.get(index, docId));
    }

    @Override
    public RedisFuture<List<Map<K, V>>> ftMget(K index, K... docIds) {
        return dispatch(commandBuilder.mget(index, docIds));
    }

    @Override
    public RedisFuture<Boolean> del(K index, K docId) {
        return dispatch(commandBuilder.del(index, docId));
    }

    @Override
    public RedisFuture<Boolean> del(K index, K docId, boolean deleteDoc) {
        return dispatch(commandBuilder.del(index, docId, deleteDoc));
    }

    @Override
    public RedisFuture<String> alter(K index, K field, FieldOptions options) {
        return dispatch(commandBuilder.alter(index, field, options));
    }

    @Override
    public RedisFuture<String> aliasAdd(K name, K index) {
        return dispatch(commandBuilder.aliasAdd(name, index));
    }

    @Override
    public RedisFuture<String> aliasDel(K name) {
        return dispatch(commandBuilder.aliasDel(name));
    }

    @Override
    public RedisFuture<String> aliasUpdate(K name, K index) {
        return dispatch(commandBuilder.aliasUpdate(name, index));
    }

}
