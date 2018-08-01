package com.redislabs.lettusearch;

import com.redislabs.lettusearch.index.Document;
import com.redislabs.lettusearch.index.SearchResults;
import com.redislabs.lettusearch.index.SearchResultsNoContent;

import io.lettuce.core.dynamic.Commands;
import io.lettuce.core.dynamic.annotation.Command;
import io.lettuce.core.dynamic.annotation.Param;

public interface IndexCommands extends Commands {

	@Command("FT.CREATE")
	boolean create(String index, Schema schema);

	@Command("FT.ADD")
	boolean add(String index, Document document);

	@Command("FT.ADDHASH")
	boolean addHash(String index, String docId, double score);

	@Command("FT.ADDHASH :index :docId :score LANGUAGE :language")
	boolean addHash(@Param("index") String index, @Param("docId") String docId, @Param("score") double score,
			@Param("language") String language);

	@Command("FT.ADDHASH :index :docId :score REPLACE")
	boolean addHashReplace(@Param("index") String index, @Param("docId") String docId, @Param("score") double score);

	@Command("FT.ADDHASH :index :docId :score LANGUAGE :language REPLACE")
	boolean addHashReplace(@Param("index") String index, @Param("docId") String docId, @Param("score") double score,
			@Param("language") String language);

//	@Command("FT.ALTER :index SCHEMA ADD :field")
//	boolean alter(@Param("index") String index, @Param("field") Field field);

	/*
	 * FT.SEARCH {index} {query} [NOCONTENT] [VERBATIM] [NOSTOPWORDS] [WITHSCORES]
	 * [WITHPAYLOADS] [WITHSORTKEYS] [FILTER {numeric_field} {min} {max}] ...
	 * [GEOFILTER {geo_field} {lon} {lat} {raius} m|km|mi|ft] [INKEYS {num} {key}
	 * ... ] [INFIELDS {num} {field} ... ] [RETURN {num} {field} ... ] [SUMMARIZE
	 * [FIELDS {num} {field} ... ] [FRAGS {num}] [LEN {fragsize}] [SEPARATOR
	 * {separator}]] [HIGHLIGHT [FIELDS {num} {field} ... ] [TAGS {open} {close}]]
	 * [SLOP {slop}] [INORDER] [LANGUAGE {language}] [EXPANDER {expander}] [SCORER
	 * {scorer}] [PAYLOAD {payload}] [SORTBY {field} [ASC|DESC]] [LIMIT offset num]
	 */
	@Command("FT.SEARCH :index :query WITHSCORES :options")
	<K, V> SearchResults<K, V> search(@Param("index") String index, @Param("query") String query,
			@Param("options") SearchOptions options);

	@Command("FT.SEARCH :index :query NOCONTENT WITHSCORES :options")
	<K, V> SearchResultsNoContent<K, V> searchNoContent(@Param("index") String index, @Param("query") String query,
			@Param("options") SearchOptions options);

}
