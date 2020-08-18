package com.redislabs.lettusearch.search;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import io.lettuce.core.internal.LettuceAssert;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
public class Document<K, V> extends LinkedHashMap<K, V> {

    private static final long serialVersionUID = 8972665675834263492L;

    private K id;
    private Double score;
    private V sortKey;
    private V payload;

    public static <K, V> DocumentBuilder<K, V> builder() {
        return new DocumentBuilder<>();
    }

    @Setter
    @Accessors(fluent = true)
    public static class DocumentBuilder<K, V> {

        private K id;
        private double score = 1;
        private V payload;
        private Map<K, V> fields = new HashMap<>();

        public DocumentBuilder<K, V> field(K name, V value) {
            fields.put(name, value);
            return this;
        }

        public Document<K, V> build() {
            LettuceAssert.notNull(id, "Id is required.");
            LettuceAssert.notNull(fields, "Fields are required.");
            Document<K, V> document = new Document<>();
            document.setId(id);
            document.setScore(score);
            document.setPayload(payload);
            document.putAll(fields);
            return document;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        Document<?, ?> document = (Document<?, ?>) o;
        return id.equals(document.id) && Objects.equals(score, document.score) && Objects.equals(sortKey, document.sortKey) && Objects.equals(payload, document.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, score, sortKey, payload);
    }
}
