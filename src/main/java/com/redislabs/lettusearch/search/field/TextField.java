package com.redislabs.lettusearch.search.field;

import static com.redislabs.lettusearch.CommandKeyword.NOSTEM;
import static com.redislabs.lettusearch.CommandKeyword.PHONETIC;
import static com.redislabs.lettusearch.CommandKeyword.TEXT;
import static com.redislabs.lettusearch.CommandKeyword.WEIGHT;

import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
public @Data class TextField extends Field {

	private Double weight;
	private boolean noStem;
	private PhoneticMatcher matcher;

	@Builder
	public TextField(String name, boolean sortable, boolean noIndex, Double weight, boolean noStem,
			PhoneticMatcher matcher) {
		super(name, sortable, noIndex);
		this.weight = weight;
		this.noStem = noStem;
		this.matcher = matcher;
	}

	@Override
	protected <K, V> void buildField(RediSearchCommandArgs<K, V> args) {
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
}
