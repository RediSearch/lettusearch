package com.redislabs.lettusearch.index;

import static com.redislabs.lettusearch.protocol.CommandKeyword.SCHEMA;

import java.util.Arrays;
import java.util.List;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.index.field.Field;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import io.lettuce.core.internal.LettuceAssert;
import lombok.Getter;

@Getter
public class Schema<K> implements RediSearchArgument {

    static final String MUST_NOT_BE_EMPTY = "must not be empty";

    private final List<Field<K>> fields;

    public Schema(List<Field<K>> fields) {
        this.fields = fields;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void build(RediSearchCommandArgs args) {
        LettuceAssert.isTrue(!fields.isEmpty(), "fields " + MUST_NOT_BE_EMPTY);
        args.add(SCHEMA);
        fields.forEach(field -> field.build(args));
    }

    public static <K> Schema<K> of(Field<K>... fields) {
        if (fields == null || fields.length == 0) {
            throw new IllegalArgumentException("No fields specified");
        }
        return new Schema<>(Arrays.asList(fields));
    }

}
