package com.redislabs.lettusearch;

import io.lettuce.core.protocol.LettuceCharsets;
import io.lettuce.core.protocol.ProtocolKeyword;

public enum CommandKeyword implements ProtocolKeyword {

	ADD, MAXTEXTFIELDS, NOOFFSETS, NOHL, NOFIELDS, NOFREQS, STOPWORDS, SCHEMA, TEXT, WEIGHT, NUMERIC, GEO, PHONETIC,
	TAG, SEPARATOR, SORTABLE, NOSTEM, NOINDEX, NOSAVE, REPLACE, PARTIAL, LANGUAGE, PAYLOAD, IF, FIELDS, NOCONTENT,
	VERBATIM, NOSTOPWORDS, FUZZY, WITHPAYLOADS, WITHSORTKEYS, WITHSCORES, MAX, LIMIT, SORTBY, ASC, DESC, INCR, KEEPDOCS,
	WITHSCHEMA, LOAD, APPLY, AS, FILTER, GROUPBY, REDUCE, COUNT, COUNT_DISTINCT, COUNT_DISTINCTISH, SUM, MIN, AVG,
	STDDEV, QUANTILE, TOLIST, FIRST_VALUE, RANDOM_SAMPLE, BY, DD, INFIELDS, RETURN, HIGHLIGHT, TAGS, WITHCURSOR,
	MAXIDLE, READ, DEL, ALIAS;

	public final byte[] bytes;

	private CommandKeyword() {
		bytes = name().getBytes(LettuceCharsets.ASCII);
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}
}
