package com.redislabs.lettusearch.search.field;

import static com.redislabs.lettusearch.CommandKeyword.GEO;

import com.redislabs.lettusearch.RediSearchCommandArgs;

public class GeoField extends Field {

	public GeoField(String name) {
		super(name);
	}

	@Override
	protected <K, V> void buildField(RediSearchCommandArgs<K, V> args) {
		args.add(GEO);
	}
}
