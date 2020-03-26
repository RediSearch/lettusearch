package com.redislabs.lettusearch.search;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public @Data class Document<K, V> extends LinkedHashMap<K, V> {

	private static final long serialVersionUID = 8972665675834263492L;

	private K id;
	private Double score;
	private Map<K, V> fields;
}
