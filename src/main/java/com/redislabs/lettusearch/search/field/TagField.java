package com.redislabs.lettusearch.search.field;

import static com.redislabs.lettusearch.protocol.CommandKeyword.SEPARATOR;
import static com.redislabs.lettusearch.protocol.CommandKeyword.TAG;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@SuperBuilder
public @Data class TagField extends Field {

	private String separator;

	@Builder
	private TagField(String name) {
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