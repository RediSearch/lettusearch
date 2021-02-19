package com.redislabs.lettusearch;

/**
 * Synchronously executed commands for RediSearch search index.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 * @since 1.0
 */
public interface SearchCommands<K, V> {

	SearchResults<K, V> search(K index, V query);

	SearchResults<K, V> search(K index, V query, SearchOptions<K> options);

}
