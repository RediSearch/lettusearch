package com.redislabs.lettusearch;

public interface RediSearchArgument {

	<K, V> void build(RediSearchCommandArgs<K, V> args);

}
