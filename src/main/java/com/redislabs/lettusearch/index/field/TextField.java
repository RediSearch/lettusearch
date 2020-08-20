package com.redislabs.lettusearch.index.field;

import static com.redislabs.lettusearch.protocol.CommandKeyword.NOSTEM;
import static com.redislabs.lettusearch.protocol.CommandKeyword.PHONETIC;
import static com.redislabs.lettusearch.protocol.CommandKeyword.TEXT;
import static com.redislabs.lettusearch.protocol.CommandKeyword.WEIGHT;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public @Getter @Setter class TextField<K> extends Field<K> {

	private Double weight;
	private boolean noStem;
	private PhoneticMatcher matcher;

	@Builder
	private TextField(K name, boolean sortable, boolean noIndex, Double weight, boolean noStem,
			PhoneticMatcher matcher) {
		super(name, sortable, noIndex);
		this.weight = weight;
		this.noStem = noStem;
		this.matcher = matcher;
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
}
