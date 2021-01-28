package com.redislabs.lettusearch.index.field;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.redislabs.lettusearch.protocol.CommandKeyword.SEPARATOR;
import static com.redislabs.lettusearch.protocol.CommandKeyword.TAG;

@Getter
@Setter
public class TagField<K> extends Field<K> {

    private String separator;

    public TagField(K name) {
        super(FieldType.TAG, name);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void buildField(RediSearchCommandArgs args) {
        args.add(TAG);
        if (separator != null) {
            args.add(SEPARATOR);
            args.add(separator);
        }
    }

    public static <K> TagFieldBuilder<K> builder(K name) {
        return new TagFieldBuilder<>(name);
    }

    @Setter
    @Accessors(fluent = true)
    public static class TagFieldBuilder<K> extends FieldBuilder<K, TagField<K>, TagFieldBuilder<K>> {

        private String separator;

        public TagFieldBuilder(K name) {
            super(name);
        }

        @Override
        protected TagField<K> newField(K name) {
            TagField<K> field = new TagField<>(name);
            field.setSeparator(separator);
            return field;
        }
    }
}