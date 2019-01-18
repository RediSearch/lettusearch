package com.redislabs.lettusearch.search;

import static com.redislabs.lettusearch.CommandKeyword.NOSTEM;
import static com.redislabs.lettusearch.CommandKeyword.WEIGHT;

import io.lettuce.core.protocol.CommandArgs;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextField extends Field {

	private Double weight;
	private boolean noStem;

	public TextField(String name) {
		super(name, FieldType.Text);
	}

	@Override
	protected <K, V> void buildField(CommandArgs<K, V> args) {
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
