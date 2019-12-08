package com.redislabs.lettusearch.search.field;

import static com.redislabs.lettusearch.CommandKeyword.NOINDEX;
import static com.redislabs.lettusearch.CommandKeyword.NOSTEM;
import static com.redislabs.lettusearch.CommandKeyword.PHONETIC;
import static com.redislabs.lettusearch.CommandKeyword.SEPARATOR;
import static com.redislabs.lettusearch.CommandKeyword.SORTABLE;
import static com.redislabs.lettusearch.CommandKeyword.WEIGHT;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public @Data class FieldOptions implements RediSearchArgument {

	private FieldType type = FieldType.Text;
	private boolean sortable;
	private boolean noIndex;
	private Double weight;
	private boolean noStem;
	private PhoneticMatcher matcher;
	private String separator;

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		args.add(type.getKeyword());
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
		if (separator != null) {
			args.add(SEPARATOR);
			args.add(separator);
		}
		if (sortable) {
			args.add(SORTABLE);
		}
		if (noIndex) {
			args.add(NOINDEX);
		}
	}

}
