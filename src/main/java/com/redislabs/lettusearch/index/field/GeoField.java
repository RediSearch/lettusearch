package com.redislabs.lettusearch.index.field;

import static com.redislabs.lettusearch.protocol.CommandKeyword.GEO;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;

public class GeoField<K> extends Field<K> {

	@Builder
	private GeoField(K name, boolean sortable, boolean noIndex) {
		super(name, sortable, noIndex);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void buildField(RediSearchCommandArgs args) {
		args.add(GEO);
	}
}
