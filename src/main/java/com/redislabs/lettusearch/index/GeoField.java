package com.redislabs.lettusearch.index;

import static com.redislabs.lettusearch.index.CommandKeyword.GEO;

import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GeoField extends Field {

	@Builder
	public GeoField(String name, boolean sortable, boolean noindex) {
		super(name, sortable, noindex);
	}

	@Override
	protected <K, V> void buildField(CommandArgs<K, V> args) {
		args.add(GEO);
		super.buildField(args);
	}
}
