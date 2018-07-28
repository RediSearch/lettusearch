package com.redislabs.lettusearch;

import java.util.List;

import io.lettuce.core.dynamic.Commands;
import io.lettuce.core.dynamic.annotation.Command;

public interface RediSearchCommands extends Commands {

	@Command("FT.CREATE")
	boolean create(String index, SchemaArgs schema);

	@Command("FT.SUGADD ?0 ?1 ?2")
	Long sugadd(String key, String string, double score);

	@Command("FT.SUGGET ?0 ?1")
	List<String> sugget(String key, String prefix);

	@Command("FT.SUGGET ?0 ?1 FUZZY")
	List<String> suggetFuzzy(String key, String prefix);

	@Command("FT.SUGDEL ?0 ?1")
	Long sugdel(String key, String string);

	@Command("FT.SUGLEN ?0")
	Long suglen(String key);

}
