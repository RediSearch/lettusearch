package com.redislabs.lettusearch;

import java.util.List;

import io.lettuce.core.dynamic.Commands;
import io.lettuce.core.dynamic.annotation.Command;

public interface SuggestionCommands extends Commands {

	@Command("FT.SUGADD ?0 ?1 ?2")
	Long add(String key, String string, double score);

	@Command("FT.SUGGET ?0 ?1")
	List<String> get(String key, String prefix);

	@Command("FT.SUGGET ?0 ?1 FUZZY")
	List<String> getFuzzy(String key, String prefix);

	@Command("FT.SUGDEL ?0 ?1")
	Long delete(String key, String string);

	@Command("FT.SUGLEN ?0")
	Long length(String key);
}
