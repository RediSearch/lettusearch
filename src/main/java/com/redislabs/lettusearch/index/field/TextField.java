package com.redislabs.lettusearch.index.field;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.redislabs.lettusearch.protocol.CommandKeyword.*;

@Getter
@Setter
public class TextField<K> extends Field<K> {

    private Double weight;
    private boolean noStem;
    private PhoneticMatcher matcher;

    public TextField(K name) {
        super(FieldType.TEXT, name);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void buildField(RediSearchCommandArgs args) {
        args.add(TEXT);
        if (noStem) {
            args.add(NOSTEM);
        }
        if (weight != null) {
            args.add(WEIGHT);
            args.add(weight);
        }
        if (matcher != null) {
            args.add(PHONETIC);
            args.add(matcher.getCode());
        }
    }

    public static <K> TextFieldBuilder<K> builder(K name) {
        return new TextFieldBuilder<>(name);
    }

    @Setter
    @Accessors(fluent = true)
    public static class TextFieldBuilder<K> extends FieldBuilder<K, TextField<K>, TextFieldBuilder<K>> {

        private Double weight;
        private boolean noStem;
        private PhoneticMatcher matcher;

        public TextFieldBuilder(K name) {
            super(name);
        }

        @Override
        protected TextField<K> newField(K name) {
            TextField<K> field = new TextField<>(name);
            field.setWeight(weight);
            field.setNoStem(noStem);
            field.setMatcher(matcher);
            return field;
        }

    }
}
