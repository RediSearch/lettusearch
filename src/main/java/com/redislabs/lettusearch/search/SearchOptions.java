package com.redislabs.lettusearch.search;

import java.util.List;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import static com.redislabs.lettusearch.protocol.CommandKeyword.*;

@Data
@Builder
public class SearchOptions<K> implements RediSearchArgument {

	private boolean noContent;
	private boolean verbatim;
	private boolean noStopWords;
	private boolean withScores;
	private boolean withPayloads;
	private boolean withSortKeys;
	@Singular
	private List<K> inKeys;
	@Singular
	private List<K> inFields;
	@Singular
	private List<K> returnFields;
	private HighlightOptions<K> highlight;
	private Language language;
	private SortBy<K> sortBy;
	private Limit limit;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void build(RediSearchCommandArgs args) {
		if (noContent) {
			args.add(NOCONTENT);
		}
		if (verbatim) {
			args.add(VERBATIM);
		}
		if (noStopWords) {
			args.add(NOSTOPWORDS);
		}
		if (withScores) {
			args.add(WITHSCORES);
		}
		if (withPayloads) {
			args.add(WITHPAYLOADS);
		}
		if (withSortKeys) {
			args.add(WITHSORTKEYS);
		}
		if (!inKeys.isEmpty()) {
			args.add(INKEYS);
			args.add(inKeys.size());
			inKeys.forEach(args::addKey);
		}
		if (!inFields.isEmpty()) {
			args.add(INFIELDS);
			args.add(inFields.size());
			inFields.forEach(args::addKey);
		}
		if (!returnFields.isEmpty()) {
			args.add(RETURN);
			args.add(returnFields.size());
			returnFields.forEach(args::addKey);
		}
		if (highlight != null) {
			args.add(HIGHLIGHT);
			highlight.build(args);
		}
		if (sortBy != null) {
			args.add(SORTBY);
			sortBy.build(args);
		}
		if (language != null) {
			args.add(LANGUAGE);
			args.add(language.name());
		}
		if (limit != null) {
			limit.build(args);
		}
	}

}
