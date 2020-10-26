package com.redislabs.lettusearch.index;

import static com.redislabs.lettusearch.protocol.CommandKeyword.FILTER;
import static com.redislabs.lettusearch.protocol.CommandKeyword.LANGUAGE;
import static com.redislabs.lettusearch.protocol.CommandKeyword.LANGUAGE_FIELD;
import static com.redislabs.lettusearch.protocol.CommandKeyword.MAXTEXTFIELDS;
import static com.redislabs.lettusearch.protocol.CommandKeyword.NOFIELDS;
import static com.redislabs.lettusearch.protocol.CommandKeyword.NOFREQS;
import static com.redislabs.lettusearch.protocol.CommandKeyword.NOHL;
import static com.redislabs.lettusearch.protocol.CommandKeyword.NOINITIALSCAN;
import static com.redislabs.lettusearch.protocol.CommandKeyword.NOOFFSETS;
import static com.redislabs.lettusearch.protocol.CommandKeyword.ON;
import static com.redislabs.lettusearch.protocol.CommandKeyword.PAYLOAD_FIELD;
import static com.redislabs.lettusearch.protocol.CommandKeyword.PREFIX;
import static com.redislabs.lettusearch.protocol.CommandKeyword.SCORE;
import static com.redislabs.lettusearch.protocol.CommandKeyword.SCORE_FIELD;
import static com.redislabs.lettusearch.protocol.CommandKeyword.STOPWORDS;
import static com.redislabs.lettusearch.protocol.CommandKeyword.TEMPORARY;

import java.util.Arrays;
import java.util.List;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;
import com.redislabs.lettusearch.search.Language;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOptions<K, V> implements RediSearchArgument {

	private Structure on;
	private List<K> prefixes;
	private String filter;
	private Language defaultLanguage;
	private K languageField;
	private Double defaultScore;
	private K scoreField;
	private K payloadField;
	private boolean maxTextFields;
	private Long temporary;
	private boolean noOffsets;
	private boolean noHL;
	private boolean noFields;
	private boolean noFreqs;
	private boolean noItitialScan;
	/**
	 * set to empty list for STOPWORDS 0
	 */
	private List<V> stopWords;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void build(RediSearchCommandArgs args) {
		if (on != null) {
			args.add(ON);
			args.add(on.name());
		}
		if (prefixes != null) {
			args.add(PREFIX);
			args.add(prefixes.size());
			prefixes.forEach(args::addKey);
		}
		if (filter != null) {
			args.add(FILTER);
			args.add(filter);
		}
		if (defaultLanguage != null) {
			args.add(LANGUAGE);
			args.add(defaultLanguage.name());
		}
		if (languageField != null) {
			args.add(LANGUAGE_FIELD);
			args.addKey(languageField);
		}
		if (defaultScore != null) {
			args.add(SCORE);
			args.add(defaultScore);
		}
		if (scoreField != null) {
			args.add(SCORE_FIELD);
			args.addKey(scoreField);
		}
		if (payloadField != null) {
			args.add(PAYLOAD_FIELD);
			args.addKey(payloadField);
		}
		if (maxTextFields) {
			args.add(MAXTEXTFIELDS);
		}
		if (temporary != null) {
			args.add(TEMPORARY);
			args.add(temporary);
		}
		if (noOffsets) {
			args.add(NOOFFSETS);
		}
		if (noHL) {
			args.add(NOHL);
		}
		if (noFields) {
			args.add(NOFIELDS);
		}
		if (noFreqs) {
			args.add(NOFREQS);
		}
		if (noItitialScan) {
			args.add(NOINITIALSCAN);
		}
		if (stopWords != null) {
			args.add(STOPWORDS);
			args.add(stopWords.size());
			stopWords.forEach(args::addValue);
		}
	}

	public static class CreateOptionsBuilder<K, V> {

		@SuppressWarnings("unchecked")
		public CreateOptionsBuilder<K, V> prefixes(K... prefixes) {
			this.prefixes = Arrays.asList(prefixes);
			return this;
		}
	}

}
