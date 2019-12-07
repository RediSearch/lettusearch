package com.redislabs.lettusearch.search.field;

import static com.redislabs.lettusearch.CommandKeyword.SEPARATOR;
import static com.redislabs.lettusearch.CommandKeyword.TAG;

import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
public @Data class TagField extends Field {

	private String separator;

	@Builder
	public TagField(String name, boolean sortable, boolean noIndex, String separator) {
		super(name, sortable, noIndex);
		this.separator = separator;
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