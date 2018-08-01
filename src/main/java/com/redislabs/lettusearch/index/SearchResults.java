package com.redislabs.lettusearch.index;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchResults<K, V> extends AbstractSearchResults<K, V> {

}
