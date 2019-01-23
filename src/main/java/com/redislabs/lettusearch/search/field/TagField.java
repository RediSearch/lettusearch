package com.redislabs.lettusearch.search.field;

import static com.redislabs.lettusearch.CommandKeyword.TAG;
import static com.redislabs.lettusearch.CommandKeyword.SEPARATOR;

import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TagField extends Field {

	private final String separator;

	@Builder
	public TagField(String name, boolean sortable, boolean noIndex, String separator) {
		super(name, sortable, noIndex);
		this.separator = separator;
	}

	@Override
	protected <K, V> void buildField(CommandArgs<K, V> args) {
		args.add(TAG);
		if (separator != null) {
			args.add(SEPARATOR);
			args.add(separator);
		}
	}
}