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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HighlightOptions<K> implements RediSearchArgument {

	@Singular
	private List<K> fields;
	private TagOptions<K> tags;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void build(RediSearchCommandArgs args) {
		if (fields.size() > 0) {
			args.add(FIELDS);
			args.add(fields.size());
			fields.forEach(args::addKey);
		}
		if (tags != null) {
			args.add(TAGS);
			args.addKey(tags.getOpen());
			args.addKey(tags.getClose());
		}
	}

}
