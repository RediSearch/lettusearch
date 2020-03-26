package com.redislabs.lettusearch.search;

import static com.redislabs.lettusearch.protocol.CommandKeyword.FIELDS;
import static com.redislabs.lettusearch.protocol.CommandKeyword.TAGS;

import java.util.List;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public @Data class HighlightOptions implements RediSearchArgument {

	@Singular
	private List<String> fields;
	private TagOptions tags;

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		if (fields.size() > 0) {
			args.add(FIELDS);
			args.add(fields.size());
			fields.forEach(f -> args.add(f));
		}
		if (tags != null) {
			args.add(TAGS);
			args.add(tags.getOpen());
			args.add(tags.getClose());
		}
	}

}
