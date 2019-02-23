package com.redislabs.lettusearch.search.field;

import static com.redislabs.lettusearch.CommandKeyword.GEO;

import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GeoField extends Field {

	@Builder
	public GeoField(String name, boolean sortable, boolean noIndex) {
		super(name, sortable, noIndex);
	}

	@Override
	protected <K, V> void buildField(RediSearchCommandArgs<K, V> args) {
		args.add(GEO);
	}
}
