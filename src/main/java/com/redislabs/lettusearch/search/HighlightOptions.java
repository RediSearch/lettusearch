package com.redislabs.lettusearch.search;

import static com.redislabs.lettusearch.CommandKeyword.FIELDS;
import static com.redislabs.lettusearch.CommandKeyword.TAGS;

import java.util.ArrayList;
import java.util.List;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public @Data class HighlightOptions implements RediSearchArgument {

	private List<String> fields = new ArrayList<>();
	private TagOptions tags;

	public HighlightOptions field(String field) {
		fields.add(field);
		return this;
	}

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
	@Accessors(fluent = true)
	public static class TagOptions {

		private String open;
		private String close;

	}
}
