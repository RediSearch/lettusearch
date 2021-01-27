package com.redislabs.lettusearch.index.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redislabs.lettusearch.index.CreateOptions;
import com.redislabs.lettusearch.index.DropOptions;
import com.redislabs.lettusearch.index.Schema;
import com.redislabs.lettusearch.index.field.FieldOptions;
import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.Document;

/**
 * Synchronously-executed index admin commands for RediSearch.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 * @since 1.0
 */
public interface IndexCommands<K, V> {

    String create(K index, Schema<K> schema);

    String create(K index, Schema<K> schema, CreateOptions<K, V> options);

    String drop(K index);

    String drop(K index, DropOptions options);

    String alter(K index, K field, FieldOptions options);

    List<Object> ftInfo(K index);

    String aliasAdd(K name, K index);

    String aliasUpdate(K name, K index);

    String aliasDel(K name);

    String add(K index, Document<K, V> document);

    String add(K index, Document<K, V> document, AddOptions options);

    boolean del(K index, K docId);

    boolean del(K index, K docId, boolean deleteDoc);

    Map<K, V> get(K index, K docId);

    @SuppressWarnings("unchecked")
    List<Map<K, V>> ftMget(K index, K... docIds);

    List<K> list();

}
