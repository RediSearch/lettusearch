package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.CommandKeyword.GROUPBY;

import java.util.List;

import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class Group extends PropertyArgument implements Operation {

	@Singular
	private List<String> properties;
	@Singular
	private List<Reduce> reduces;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		args.add(GROUPBY);
		args.add(properties.size());
		properties.forEach(property -> args.add(prefix(property)));
		reduces.forEach(reduce -> reduce.build(args));
	}

}
