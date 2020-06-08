package com.redislabs.lettusearch.protocol;

import java.nio.charset.StandardCharsets;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import io.lettuce.core.protocol.ProtocolKeyword;

public enum CommandKeyword implements ProtocolKeyword {

	ADD, MAXTEXTFIELDS, TEMPORARY, NOOFFSETS, NOHL, NOFIELDS, NOFREQS, STOPWORDS, SCHEMA, TEXT, WEIGHT, NUMERIC, GEO,
	PHONETIC, TAG, SEPARATOR, SORTABLE, NOSTEM, NOINDEX, NOSAVE, REPLACE, PARTIAL, LANGUAGE, PAYLOAD, IF, FIELDS,
	NOCONTENT, VERBATIM, NOSTOPWORDS, FUZZY, WITHPAYLOADS, WITHSORTKEYS, WITHSCORES, MAX, LIMIT, SORTBY, ASC, DESC,
	INCR, KEEPDOCS, WITHSCHEMA, LOAD, APPLY, AS, FILTER, GROUPBY, REDUCE, COUNT, COUNT_DISTINCT, COUNT_DISTINCTISH, SUM,
	MIN, AVG, STDDEV, QUANTILE, TOLIST, FIRST_VALUE, RANDOM_SAMPLE, BY, DD, INKEYS, INFIELDS, RETURN, HIGHLIGHT, TAGS,
	WITHCURSOR, MAXIDLE, READ, DEL, ALIAS;

	public final byte[] bytes;

	CommandKeyword() {
		bytes = name().getBytes(StandardCharsets.US_ASCII);
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}
}
