package com.redislabs.lettusearch.search;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class SearchResults<K, V> extends ArrayList<Document<K, V>> {

	private static final long serialVersionUID = 286617386389045710L;

	private @Getter @Setter long count;

}
