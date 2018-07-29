package com.redislabs.lettusearch.index;

import static com.redislabs.lettusearch.index.CommandKeyword.MAXTEXTFIELDS;
import static com.redislabs.lettusearch.index.CommandKeyword.NOFIELDS;
import static com.redislabs.lettusearch.index.CommandKeyword.NOFREQS;
import static com.redislabs.lettusearch.index.CommandKeyword.NOHL;
import static com.redislabs.lettusearch.index.CommandKeyword.NOOFFSETS;
import static com.redislabs.lettusearch.index.CommandKeyword.SCHEMA;
import static com.redislabs.lettusearch.index.CommandKeyword.STOPWORDS;

import java.util.List;

import io.lettuce.core.CompositeArgument;
import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
public class Schema implements CompositeArgument {

	boolean maxTextFields;
	boolean noOffsets;
	boolean noHL;
	boolean noFields;
	boolean noFreqs;
	@Singular
	List<String> stopWords;
	@Singular
	List<Field> fields;

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
		fields.forEach(field -> field.build(args));
	}
}
