package com.redislabs.lettusearch.search;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagOptions<K> {

	private K open;
	private K close;

}