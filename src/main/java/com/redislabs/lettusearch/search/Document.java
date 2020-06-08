package com.redislabs.lettusearch.search;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import io.lettuce.core.internal.LettuceAssert;
import lombok.*;
import lombok.experimental.Accessors;

public class Document<K, V> extends LinkedHashMap<K, V> {

    private static final long serialVersionUID = 8972665675834263492L;

    @Getter
    @Setter
    private K id;
    @Getter
    @Setter
    private Double score;
    @Getter
    @Setter
    private V payload;

    public static DocumentBuilder builder() {
        return new DocumentBuilder();
    }

    @Setter
    @Accessors(fluent = true)
    public static class DocumentBuilder {

        private String id;
        private double score = 1;
        private String payload;
        private Map<String, String> fields = new HashMap<>();

        public DocumentBuilder field(String name, String value) {
            fields.put(name, value);
            return this;
        }

        public Document<String, String> build() {
            LettuceAssert.notNull(id, "Id is required.");
            LettuceAssert.notNull(fields, "Fields are required.");
            Document<String, String> document = new Document<>();
            document.setId(id);
            document.setScore(score);
            document.setPayload(payload);
            document.putAll(fields);
            return document;
        }

    }

}
