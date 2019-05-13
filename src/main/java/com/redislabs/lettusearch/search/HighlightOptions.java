package com.redislabs.lettusearch.search;

import static com.redislabs.lettusearch.CommandKeyword.FIELDS;
import static com.redislabs.lettusearch.CommandKeyword.TAGS;

import java.util.List;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
public class HighlightOptions implements RediSearchArgument {

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
			args.add(tags.open);
			args.add(tags.close);
		}
	}

	@Data
	@Builder
	public static class TagOptions {

		private String open;
		private String close;

	}
}
