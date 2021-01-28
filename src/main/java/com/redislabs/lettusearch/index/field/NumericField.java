package com.redislabs.lettusearch.index.field;

import static com.redislabs.lettusearch.protocol.CommandKeyword.NUMERIC;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;

public class NumericField<K> extends Field<K> {

    public NumericField(K name) {
        super(FieldType.NUMERIC, name);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void buildField(RediSearchCommandArgs args) {
        args.add(NUMERIC);
    }

    public static <K> NumericFieldBuilder<K> builder(K name) {
        return new NumericFieldBuilder<>(name);
    }

    public static class NumericFieldBuilder<K> extends FieldBuilder<K, NumericField<K>, NumericFieldBuilder<K>> {

        public NumericFieldBuilder(K name) {
            super(name);
        }

        @Override
        protected NumericField<K> newField(K name) {
            return new NumericField<>(name);
        }

    }
}
