package com.redislabs.lettusearch.index.field;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;
import io.lettuce.core.internal.LettuceAssert;
import lombok.Getter;
import lombok.Setter;

import static com.redislabs.lettusearch.protocol.CommandKeyword.NOINDEX;
import static com.redislabs.lettusearch.protocol.CommandKeyword.SORTABLE;

@Getter
@Setter
public abstract class Field<K> implements RediSearchArgument {

    static final String MUST_NOT_BE_EMPTY = "must not be empty";
    static final String MUST_NOT_BE_NULL = "must not be null";

    private final FieldType type;
    private final K name;
    private boolean sortable;
    private boolean noIndex;

    protected Field(FieldType type, K name) {
        this.type = type;
        this.name = name;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void build(RediSearchCommandArgs args) {
        LettuceAssert.notNull(name, "name " + MUST_NOT_BE_NULL);
        args.addKey(name);
        buildField(args);
        if (sortable) {
            args.add(SORTABLE);
        }
        if (noIndex) {
            args.add(NOINDEX);
        }
    }

    @SuppressWarnings("rawtypes")
    protected abstract void buildField(RediSearchCommandArgs args);

    protected static abstract class FieldBuilder<K, F extends Field<K>, B extends FieldBuilder<K, F, B>> {

        private final K name;
        private boolean sortable;
        private boolean noIndex;

        protected FieldBuilder(K name) {
            this.name = name;
        }

        public B sortable(boolean sortable) {
            this.sortable = sortable;
            return (B) this;
        }

        public B noIndex(boolean noIndex) {
            this.noIndex = noIndex;
            return (B) this;
        }

        public F build() {
            F field = newField(name);
            field.setNoIndex(noIndex);
            field.setSortable(sortable);
            return field;
        }

        protected abstract F newField(K name);

    }

    public static <K> TextField.TextFieldBuilder<K> text(K name) {
        return TextField.builder(name);
    }

    public static <K> GeoField.GeoFieldBuilder<K> geo(K name) {
        return GeoField.builder(name);
    }

    public static <K> TagField.TagFieldBuilder<K> tag(K name) {
        return TagField.builder(name);
    }

    public static <K> NumericField.NumericFieldBuilder<K> numeric(K name) {
        return NumericField.builder(name);
    }
}