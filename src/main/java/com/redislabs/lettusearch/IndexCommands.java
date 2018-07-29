package com.redislabs.lettusearch;

import com.redislabs.lettusearch.index.Document;
import com.redislabs.lettusearch.index.Schema;

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
}
