package com.redislabs.lettusearch.aggregate.reducer;

import com.redislabs.lettusearch.aggregate.Reducer;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Getter;
import lombok.Setter;

public abstract @Getter @Setter class AbstractPropertyReducer extends Reducer {

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
