package com.redislabs.lettusearch.search.field;

import static com.redislabs.lettusearch.CommandKeyword.NOSTEM;
import static com.redislabs.lettusearch.CommandKeyword.PHONETIC;
import static com.redislabs.lettusearch.CommandKeyword.TEXT;
import static com.redislabs.lettusearch.CommandKeyword.WEIGHT;

import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class TextField extends Field {

	@Getter
	@Setter
	private Double weight;
	@Getter
	@Setter
	private boolean noStem;
	@Getter
	@Setter
	private PhoneticMatcher matcher;

	public TextField(String name) {
		super(name);
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
