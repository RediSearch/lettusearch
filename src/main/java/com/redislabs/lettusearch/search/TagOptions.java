package com.redislabs.lettusearch.search;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagOptions {

	private String open;
	private String close;

}