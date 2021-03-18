package com.redislabs.lettusearch.impl;

import com.redislabs.lettusearch.*;
import io.lettuce.core.RedisReactiveCommandsImpl;
import io.lettuce.core.codec.RedisCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RediSearchReactiveCommandsImpl<K, V> extends RedisReactiveCommandsImpl<K, V> implements RediSearchReactiveCommands<K, V> {

    private final StatefulRediSearchConnection<K, V> connection;
    private final RediSearchCommandBuilder<K, V> commandBuilder;

    public RediSearchReactiveCommandsImpl(StatefulRediSearchConnection<K, V> connection, RedisCodec<K, V> codec) {
        super(connection, codec);
        this.connection = connection;
        this.commandBuilder = new RediSearchCommandBuilder<>(codec);
    }

    @Override
    public StatefulRediSearchConnection<K, V> getStatefulConnection() {
        return connection;
    }

    @Override
    public Mono<String> create(K index, Field<K>... fields) {
        return create(index, null, fields);
    }

    @Override
    public Mono<String> create(K index, CreateOptions<K, V> options, Field<K>... fields) {
        return createMono(() -> commandBuilder.create(index, options, fields));
    }

    @Override
    public Mono<String> dropIndex(K index) {
        return dropIndex(index, false);
    }

    @Override
    public Mono<String> dropIndex(K index, boolean deleteDocs) {
        return createMono(() -> commandBuilder.dropIndex(index, deleteDocs));
    }

    @Override
    public Flux<Object> ftInfo(K index) {
        return createDissolvingFlux(() -> commandBuilder.info(index));
    }

    @Override
    public Mono<SearchResults<K, V>> search(K index, V query) {
        return createMono(() -> commandBuilder.search(index, query, null));
    }

    @Override
    public Mono<SearchResults<K, V>> search(K index, V query, SearchOptions<K> options) {
        return createMono(() -> commandBuilder.search(index, query, options));
    }

    @Override
    public Mono<AggregateResults<K>> aggregate(K index, V query) {
        return createMono(() -> commandBuilder.aggregate(index, query, null));
    }

    @Override
    public Mono<AggregateResults<K>> aggregate(K index, V query, AggregateOptions options) {
        return createMono(() -> commandBuilder.aggregate(index, query, options));
    }

    @Override
    public Mono<AggregateWithCursorResults<K>> aggregate(K index, V query, Cursor cursor) {
        return createMono(() -> commandBuilder.aggregate(index, query, cursor, null));
    }

    @Override
    public Mono<AggregateWithCursorResults<K>> aggregate(K index, V query, Cursor cursor, AggregateOptions options) {
        return createMono(() -> commandBuilder.aggregate(index, query, cursor, options));
    }

    @Override
    public Mono<AggregateWithCursorResults<K>> cursorRead(K index, long cursor) {
        return createMono(() -> commandBuilder.cursorRead(index, cursor, null));
    }

    @Override
    public Mono<AggregateWithCursorResults<K>> cursorRead(K index, long cursor, long count) {
        return createMono(() -> commandBuilder.cursorRead(index, cursor, count));
    }

    @Override
    public Mono<String> cursorDelete(K index, long cursor) {
        return createMono(() -> commandBuilder.cursorDelete(index, cursor));
    }

    @Override
    public Mono<Long> sugadd(K key, Suggestion<V> suggestion) {
        return createMono(() -> commandBuilder.sugadd(key, suggestion));
    }

    @Override
    public Mono<Long> sugadd(K key, Suggestion<V> suggestion, boolean increment) {
        return createMono(() -> commandBuilder.sugadd(key, suggestion, increment));
    }

    @Override
    public Flux<Suggestion<V>> sugget(K key, V prefix) {
        return createDissolvingFlux(() -> commandBuilder.sugget(key, prefix));
    }

    @Override
    public Flux<Suggestion<V>> sugget(K key, V prefix, SuggetOptions options) {
        return createDissolvingFlux(() -> commandBuilder.sugget(key, prefix, options));
    }

    @Override
    public Mono<Boolean> sugdel(K key, V string) {
        return createMono(() -> commandBuilder.sugdel(key, string));
    }

    @Override
    public Mono<Long> suglen(K key) {
        return createMono(() -> commandBuilder.suglen(key));
    }

    @Override
    public Mono<String> alter(K index, Field<K> field) {
        return createMono(() -> commandBuilder.alter(index, field));
    }

    @Override
    public Mono<String> aliasAdd(K name, K index) {
        return createMono(() -> commandBuilder.aliasAdd(name, index));
    }

    @Override
    public Mono<String> aliasUpdate(K name, K index) {
        return createMono(() -> commandBuilder.aliasUpdate(name, index));
    }

    @Override
    public Mono<String> aliasDel(K name) {
        return createMono(() -> commandBuilder.aliasDel(name));
    }

    @Override
    public Flux<K> list() {
        return createDissolvingFlux(commandBuilder::list);
    }
}
