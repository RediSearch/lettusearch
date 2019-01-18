package com.redislabs.lettusearch.search;

import static com.redislabs.lettusearch.CommandKeyword.*;

import java.util.List;

import io.lettuce.core.CompositeArgument;
import io.lettuce.core.internal.LettuceAssert;
import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
public class Schema implements CompositeArgument {

	static final String MUST_NOT_BE_EMPTY = "must not be empty";

	private boolean maxTextFields;
	private boolean noOffsets;
	private boolean noHL;
	private boolean noFields;
	private boolean noFreqs;
	@Singular
	private List<String> stopWords;
	@Singular
	private List<Field> fields;

	public static class SchemaBuilder {

		public SchemaBuilder textField(String name, boolean sortable) {
			TextField field = new TextField(name);
			field.setSortable(sortable);
			return field(field);
		}

		public SchemaBuilder numericField(String name, boolean sortable) {
			Field field = new Field(name, FieldType.Numeric);
			field.setSortable(sortable);
			return field(field);
		}
	}

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		if (maxTextFields) {
			args.add(MAXTEXTFIELDS);
		}
		if (noOffsets) {
			args.add(NOOFFSETS);
		}
		if (noHL) {
			args.add(NOHL);
		}
		if (noFields) {
			args.add(NOFIELDS);
		}
		if (noFreqs) {
			args.add(NOFREQS);
		}
		if (!stopWords.isEmpty()) {
			args.add(STOPWORDS);
			args.add(stopWords.size());
			stopWords.forEach(stopWord -> args.add(stopWord));
		}
		args.add(SCHEMA);
		LettuceAssert.isTrue(!fields.isEmpty(), "fields " + MUST_NOT_BE_EMPTY);
		fields.forEach(field -> field.build(args));
	}

}
