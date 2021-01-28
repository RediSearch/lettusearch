package com.redislabs.lettusearch.index.field;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import static com.redislabs.lettusearch.protocol.CommandKeyword.GEO;

public class GeoField<K> extends Field<K> {

    public GeoField(K name) {
        super(FieldType.GEO, name);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void buildField(RediSearchCommandArgs args) {
        args.add(GEO);
    }

    public static <K> GeoFieldBuilder<K> builder(K name) {
        return new GeoFieldBuilder<>(name);
    }

    public static class GeoFieldBuilder<K> extends FieldBuilder<K, GeoField<K>, GeoFieldBuilder<K>> {

        public GeoFieldBuilder(K name) {
            super(name);
        }

        @Override
        protected GeoField<K> newField(K name) {
            return new GeoField<>(name);
        }
    }
}
