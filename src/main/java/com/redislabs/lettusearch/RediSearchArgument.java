package com.redislabs.lettusearch;

import com.redislabs.lettusearch.impl.protocol.RediSearchCommandArgs;

public interface RediSearchArgument {

	<K, V> void build(RediSearchCommandArgs<K, V> args);

}
