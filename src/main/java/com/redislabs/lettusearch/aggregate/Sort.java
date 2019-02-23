package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.CommandKeyword.MAX;
import static com.redislabs.lettusearch.CommandKeyword.SORTBY;

import java.util.List;

import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
public class Sort implements Operation {

	@Singular
	private List<SortProperty> properties;
	private Long max;

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		args.add(SORTBY);
		args.add(properties.size() * 2);
		properties.forEach(property -> property.build(args));
		if (max != null) {
			args.add(MAX);
			args.add(max);
		}

	}

}
