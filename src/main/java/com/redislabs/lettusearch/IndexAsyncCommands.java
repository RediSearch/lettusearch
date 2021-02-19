package com.redislabs.lettusearch;

import java.util.List;
import java.util.Map;

import io.lettuce.core.RedisFuture;

/**
 * Asynchronously-executded index administration commands for RediSearch.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 * @since 1.0
 */
public interface IndexAsyncCommands<K, V> {

    RedisFuture<String> create(K index, Field<K>... fields);

    RedisFuture<String> create(K index, CreateOptions<K, V> options, Field<K>... fields);

    RedisFuture<String> dropIndex(K index);

    RedisFuture<String> dropIndex(K index, boolean deleteDocs);

    RedisFuture<String> alter(K index, Field<K> field);

    RedisFuture<List<Object>> ftInfo(K index);

    RedisFuture<String> aliasAdd(K name, K index);

    RedisFuture<String> aliasUpdate(K name, K index);

    RedisFuture<String> aliasDel(K name);

    RedisFuture<List<K>> list();

}
