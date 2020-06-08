package com.redislabs.lettusearch;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

public interface RediSearchArgument {

    <K,V> void build(RediSearchCommandArgs<K, V> args);

}
