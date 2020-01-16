package com.redislabs.lettusearch.aggregate.reducer;

import com.redislabs.lettusearch.aggregate.Reducer;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@SuperBuilder
@NoArgsConstructor
public abstract @Data class AbstractPropertyReducer extends Reducer {

	private String property;

	protected AbstractPropertyReducer(String as, String property) {
		super(as);
		this.property = property;
	}

	@Override
	protected <K, V> void buildFunction(RediSearchCommandArgs<K, V> args) {
		buildFunction(args, property);
	}

	protected abstract <K, V> void buildFunction(RediSearchCommandArgs<K, V> args, String property);

}
