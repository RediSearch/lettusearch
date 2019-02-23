package com.redislabs.lettusearch.search;

import static com.redislabs.lettusearch.CommandKeyword.MAXTEXTFIELDS;
import static com.redislabs.lettusearch.CommandKeyword.NOFIELDS;
import static com.redislabs.lettusearch.CommandKeyword.NOFREQS;
import static com.redislabs.lettusearch.CommandKeyword.NOHL;
import static com.redislabs.lettusearch.CommandKeyword.NOOFFSETS;
import static com.redislabs.lettusearch.CommandKeyword.SCHEMA;
import static com.redislabs.lettusearch.CommandKeyword.STOPWORDS;

import java.util.List;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.RediSearchCommandArgs;
import com.redislabs.lettusearch.search.field.Field;

import io.lettuce.core.internal.LettuceAssert;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
public class Schema implements RediSearchArgument {

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

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
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
