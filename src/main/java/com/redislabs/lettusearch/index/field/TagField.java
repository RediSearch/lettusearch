package com.redislabs.lettusearch.index.field;

import static com.redislabs.lettusearch.protocol.CommandKeyword.SEPARATOR;
import static com.redislabs.lettusearch.protocol.CommandKeyword.TAG;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public @Getter @Setter class TagField<K> extends Field<K> {

	private String separator;

	@Builder
	private TagField(K name, boolean sortable, boolean noIndex, String separator) {
		super(name, sortable, noIndex);
		this.separator = separator;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void buildField(RediSearchCommandArgs args) {
		args.add(TAG);
		if (separator != null) {
			args.add(SEPARATOR);
			args.add(separator);
		}
	}
}