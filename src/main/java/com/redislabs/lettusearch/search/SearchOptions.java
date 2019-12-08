package com.redislabs.lettusearch.search;

import static com.redislabs.lettusearch.CommandKeyword.HIGHLIGHT;
import static com.redislabs.lettusearch.CommandKeyword.INFIELDS;
import static com.redislabs.lettusearch.CommandKeyword.LANGUAGE;
import static com.redislabs.lettusearch.CommandKeyword.NOCONTENT;
import static com.redislabs.lettusearch.CommandKeyword.NOSTOPWORDS;
import static com.redislabs.lettusearch.CommandKeyword.RETURN;
import static com.redislabs.lettusearch.CommandKeyword.SORTBY;
import static com.redislabs.lettusearch.CommandKeyword.VERBATIM;
import static com.redislabs.lettusearch.CommandKeyword.WITHPAYLOADS;
import static com.redislabs.lettusearch.CommandKeyword.WITHSCORES;
import static com.redislabs.lettusearch.CommandKeyword.WITHSORTKEYS;

import java.util.ArrayList;
import java.util.List;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public @Data class SearchOptions implements RediSearchArgument {

	private boolean noContent;
	private boolean verbatim;
	private boolean noStopWords;
	private boolean withScores;
	private boolean withPayloads;
	private boolean withSortKeys;
	private List<String> inFields = new ArrayList<>();
	private List<String> returnFields = new ArrayList<>();
	private HighlightOptions highlight;
	private String language;
	private SortBy sortBy;
	private Limit limit;
	
	public SearchOptions inField(String inField) {
		inFields.add(inField);
		return this;
	}
	
	public SearchOptions returnField(String returnField) {
		returnFields.add(returnField);
		return this;
	}

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
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
		if (!inFields.isEmpty()) {
			args.add(INFIELDS);
			args.add(inFields.size());
			inFields.forEach(f -> args.add(f));
		}
		if (!returnFields.isEmpty()) {
			args.add(RETURN);
			args.add(returnFields.size());
			returnFields.forEach(f -> args.add(f));
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
			args.add(language);
		}
		if (limit != null) {
			limit.build(args);
		}
	}

}
