package com.redislabs.lettusearch.search.field;

import static com.redislabs.lettusearch.CommandKeyword.SEPARATOR;
import static com.redislabs.lettusearch.CommandKeyword.TAG;

import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class TagField extends Field {

	@Getter
	@Setter
	private String separator;

	public TagField(String name) {
		super(name);
	}

	@Override
	protected <K, V> void buildField(RediSearchCommandArgs<K, V> args) {
		args.add(TAG);
		if (separator != null) {
			args.add(SEPARATOR);
			args.add(separator);
		}
	}
}