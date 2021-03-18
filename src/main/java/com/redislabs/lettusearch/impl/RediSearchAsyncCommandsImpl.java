package com.redislabs.lettusearch.impl;

import com.redislabs.lettusearch.*;
import io.lettuce.core.RedisAsyncCommandsImpl;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.codec.RedisCodec;

import java.util.List;

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
    public RedisFuture<String> create(K index, Field<K>... fields) {
        return create(index, null, fields);
    }

    @Override
    public RedisFuture<String> create(K index, CreateOptions<K, V> options, Field<K>... fields) {
        return dispatch(commandBuilder.create(index, options, fields));
    }

    @Override
    public RedisFuture<String> dropIndex(K index) {
        return dropIndex(index, false);
    }

    @Override
    public RedisFuture<String> dropIndex(K index, boolean deleteDocs) {
        return dispatch(commandBuilder.dropIndex(index, deleteDocs));
    }

    @Override
    public RedisFuture<List<Object>> ftInfo(K index) {
        return dispatch(commandBuilder.info(index));
    }

    @Override
    public RedisFuture<SearchResults<K, V>> search(K index, V query) {
        return dispatch(commandBuilder.search(index, query, null));
    }

    @Override
    public RedisFuture<SearchResults<K, V>> search(K index, V query, SearchOptions<K> options) {
        return dispatch(commandBuilder.search(index, query, options));
    }

    @Override
    public RedisFuture<AggregateResults<K>> aggregate(K index, V query) {
        return dispatch(commandBuilder.aggregate(index, query, null));
    }

    @Override
    public RedisFuture<AggregateResults<K>> aggregate(K index, V query, AggregateOptions options) {
        return dispatch(commandBuilder.aggregate(index, query, options));
    }

    @Override
    public RedisFuture<AggregateWithCursorResults<K>> aggregate(K index, V query, Cursor cursor) {
        return dispatch(commandBuilder.aggregate(index, query, cursor, null));
    }

    @Override
    public RedisFuture<AggregateWithCursorResults<K>> aggregate(K index, V query, Cursor cursor, AggregateOptions options) {
        return dispatch(commandBuilder.aggregate(index, query, cursor, options));
    }

    @Override
    public RedisFuture<AggregateWithCursorResults<K>> cursorRead(K index, long cursor) {
        return dispatch(commandBuilder.cursorRead(index, cursor, null));
    }

    @Override
    public RedisFuture<AggregateWithCursorResults<K>> cursorRead(K index, long cursor, long count) {
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
    public RedisFuture<String> alter(K index, Field<K> field) {
        return dispatch(commandBuilder.alter(index, field));
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

    @Override
    public RedisFuture<List<K>> list() {
        return dispatch(commandBuilder.list());
    }
}
