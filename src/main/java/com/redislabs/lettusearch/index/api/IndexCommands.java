package com.redislabs.lettusearch.index.api;

import java.util.List;

import com.redislabs.lettusearch.index.CreateOptions;
import com.redislabs.lettusearch.index.DropOptions;
import com.redislabs.lettusearch.index.Schema;
import com.redislabs.lettusearch.index.field.FieldOptions;

/**
 * Synchronously-executed index admin commands for RediSearch.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 * @since 1.0
 */
public interface IndexCommands<K, V> {

	String create(K index, Schema schema);

	String create(K index, Schema schema, CreateOptions options);

	String drop(K index);

	String drop(K index, DropOptions options);

	String alter(K index, K field, FieldOptions options);

	List<Object> ftInfo(K index);

	String aliasAdd(K name, K index);

	String aliasUpdate(K name, K index);

	String aliasDel(K name);

}
