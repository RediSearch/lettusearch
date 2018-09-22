package com.redislabs.lettusearch.index;

import static com.redislabs.lettusearch.index.CommandKeyword.NOSTEM;
import static com.redislabs.lettusearch.index.CommandKeyword.TEXT;
import static com.redislabs.lettusearch.index.CommandKeyword.WEIGHT;

import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TextField extends Field {

	Double weight;
	boolean noStem;

	@Builder
	public TextField(String name, boolean sortable, boolean noindex, Double weight, boolean noStem) {
		super(name, sortable, noindex);
		this.weight = weight;
		this.noStem = noStem;
	}

	@Override
	protected <K, V> void buildField(CommandArgs<K, V> args) {
		args.add(TEXT);
		if (noStem) {
			args.add(NOSTEM);
		}
		if (weight != null) {
			args.add(WEIGHT);
			args.add(weight);
		}
		super.buildField(args);
	}
}
