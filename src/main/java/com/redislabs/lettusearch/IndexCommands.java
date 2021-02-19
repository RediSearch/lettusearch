package com.redislabs.lettusearch;

import java.util.List;

/**
 * Synchronously-executed index admin commands for RediSearch.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 * @since 1.0
 */
public interface IndexCommands<K, V> {

    String create(K index, Field<K>... fields);

    String create(K index, CreateOptions<K, V> options, Field<K>... fields);

    String dropIndex(K index);

    String dropIndex(K index, boolean deleteDocs);

    String alter(K index, Field<K> field);

    List<Object> ftInfo(K index);

    String aliasAdd(K name, K index);

    String aliasUpdate(K name, K index);

    String aliasDel(K name);

    List<K> list();

}
