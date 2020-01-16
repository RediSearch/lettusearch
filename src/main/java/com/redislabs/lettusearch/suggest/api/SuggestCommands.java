package com.redislabs.lettusearch.suggest.api;

import java.util.List;

import com.redislabs.lettusearch.suggest.SuggestGetOptions;
import com.redislabs.lettusearch.suggest.SuggestResult;

/**
 * Synchronously executed commands for RediSearch suggestion index.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 * @since 1.0
 */
public interface SuggestCommands<K, V> {

	Long sugadd(K key, V string, double score);

	Long sugadd(K key, V string, double score, V payload);

	Long sugadd(K key, V string, double score, boolean increment);

	Long sugadd(K key, V string, double score, boolean increment, V payload);

	List<SuggestResult<V>> sugget(K key, V prefix, SuggestGetOptions options);

	Boolean sugdel(K key, V string);

	Long suglen(K key);
}
