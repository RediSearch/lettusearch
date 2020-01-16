package com.redislabs.lettusearch.index.api;

import java.util.List;

import com.redislabs.lettusearch.search.CreateOptions;
import com.redislabs.lettusearch.search.DropOptions;
import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.field.FieldOptions;

/**
 * Synchronously-executed index admin commands for RediSearch.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 * @since 1.0
 */
public interface IndexCommands<K, V> {

	String create(String index, Schema schema);

	String create(String index, Schema schema, CreateOptions options);

	String drop(String index);

	String drop(String index, DropOptions options);

	String alter(String index, K field, FieldOptions options);

	List<Object> indexInfo(String index);

	String aliasAdd(String name, String index);

	String aliasUpdate(String name, String index);

	String aliasDel(String name);

}
