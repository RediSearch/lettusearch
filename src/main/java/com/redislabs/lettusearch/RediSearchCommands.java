package com.redislabs.lettusearch;

import java.util.List;
import java.util.Map;

import com.redislabs.lettusearch.index.Document;
import com.redislabs.lettusearch.index.Field;
import com.redislabs.lettusearch.index.Schema;
import com.redislabs.lettusearch.index.SearchOptions;
import com.redislabs.lettusearch.index.SearchResults;
import com.redislabs.lettusearch.index.SearchResultsNoContent;
import com.redislabs.lettusearch.suggest.SuggestionOptions;

import io.lettuce.core.dynamic.Commands;
import io.lettuce.core.dynamic.annotation.Command;
import io.lettuce.core.dynamic.annotation.Param;

public interface RediSearchCommands extends Commands {

	@Command("FT.CREATE")
	boolean create(String index, Schema schema);

	@Command("FT.DROP ?0")
	boolean drop(String index);

	@Command("FT.DROP ?0 KEEPDOCS")
	boolean dropKeepDocs(String index);

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

	@Command("FT.ALTER :index SCHEMA ADD :field")
	boolean alter(@Param("index") String index, @Param("field") Field field);

	@Command("FT.SEARCH :index :query WITHSCORES :options")
	<K, V> SearchResults<K, V> search(@Param("index") String index, @Param("query") String query,
			@Param("options") SearchOptions options);

	@Command("FT.SEARCH :index :query NOCONTENT WITHSCORES :options")
	<K, V> SearchResultsNoContent<K, V> searchNoContent(@Param("index") String index, @Param("query") String query,
			@Param("options") SearchOptions options);

	@Command("FT.SUGADD ?0 ?1 ?2")
	Long suggestionAdd(String key, String string, double score);

	@Command("FT.SUGADD ?0 ?1 ?2 PAYLOAD ?3")
	Long suggestionAddPayload(String key, String string, double score, String payload);

	@Command("FT.SUGADD ?0 ?1 ?2 INCR")
	Long suggestionAddIncr(String key, String string, double score);

	@Command("FT.SUGADD ?0 ?1 ?2 INCR PAYLOAD ?3")
	Long suggestionAddIncrPayload(String key, String string, double score, String payload);

	@Command("FT.SUGGET ?0 ?1 :options")
	List<String> suggestionGet(String key, String prefix, @Param("options") SuggestionOptions options);

	@Command("FT.SUGGET ?0 ?1 WITHPAYLOADS :options")
	Map<String, String> suggestionGetWithPayloads(String key, String prefix,
			@Param("options") SuggestionOptions options);

	@Command("FT.SUGDEL ?0 ?1")
	Long suggestionDelete(String key, String string);

	@Command("FT.SUGLEN ?0")
	Long suggestionLength(String key);
}
