/*
 * Copyright 2011-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redislabs.lettusearch.impl;

import static io.lettuce.core.protocol.CommandType.AUTH;
import static io.lettuce.core.protocol.CommandType.DISCARD;
import static io.lettuce.core.protocol.CommandType.EXEC;
import static io.lettuce.core.protocol.CommandType.MULTI;
import static io.lettuce.core.protocol.CommandType.READONLY;
import static io.lettuce.core.protocol.CommandType.READWRITE;
import static io.lettuce.core.protocol.CommandType.SELECT;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.redislabs.lettusearch.ConnectionState;
import com.redislabs.lettusearch.RediSearchAsyncCommands;
import com.redislabs.lettusearch.RediSearchCommands;
import com.redislabs.lettusearch.RediSearchReactiveCommands;
import com.redislabs.lettusearch.StatefulRediSearchConnection;

import io.lettuce.core.RedisChannelHandler;
import io.lettuce.core.RedisChannelWriter;
import io.lettuce.core.api.push.PushListener;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.MultiOutput;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.AsyncCommand;
import io.lettuce.core.protocol.Command;
import io.lettuce.core.protocol.CommandArgs;
import io.lettuce.core.protocol.CommandArgsAccessor;
import io.lettuce.core.protocol.CommandKeyword;
import io.lettuce.core.protocol.CommandType;
import io.lettuce.core.protocol.CompleteableCommand;
import io.lettuce.core.protocol.ConnectionWatchdog;
import io.lettuce.core.protocol.PushHandler;
import io.lettuce.core.protocol.RedisCommand;
import io.lettuce.core.protocol.TransactionalCommand;

/**
 * A thread-safe connection to a Redis server. Multiple threads may share one
 * {@link StatefulRediSearchConnectionImpl}
 *
 * A {@link ConnectionWatchdog} monitors each connection and reconnects
 * automatically until {@link #close} is called. All pending commands will be
 * (re)sent after successful reconnection.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Mark Paluch
 */
public class StatefulRediSearchConnectionImpl<K, V> extends RedisChannelHandler<K, V>
		implements StatefulRediSearchConnection<K, V> {

	protected final RedisCodec<K, V> codec;

	protected final RediSearchCommands<K, V> sync;

	protected final RediSearchAsyncCommandsImpl<K, V> async;

	protected final RediSearchReactiveCommandsImpl<K, V> reactive;

	private final ConnectionState state = new ConnectionState();

	private final PushHandler pushHandler;

	protected MultiOutput<K, V> multi;

	/**
	 * Initialize a new connection.
	 *
	 * @param writer      the channel writer.
	 * @param pushHandler the handler for push notifications.
	 * @param codec       Codec used to encode/decode keys and values.
	 * @param timeout     Maximum time to wait for a response.
	 */
	public StatefulRediSearchConnectionImpl(RedisChannelWriter writer, PushHandler pushHandler, RedisCodec<K, V> codec,
			Duration timeout) {

		super(writer, timeout);

		this.pushHandler = pushHandler;
		this.codec = codec;
		this.async = newRediSearchAsyncCommandsImpl();
		this.sync = newRediSearchSyncCommandsImpl();
		this.reactive = newRediSearchReactiveCommandsImpl();
	}

	public RedisCodec<K, V> getCodec() {
		return codec;
	}

	@Override
	public RediSearchAsyncCommands<K, V> async() {
		return async;
	}

	/**
	 * Create a new instance of {@link RediSearchCommands}. Can be overriden to extend.
	 *
	 * @return a new instance
	 */
	protected RediSearchCommands<K, V> newRediSearchSyncCommandsImpl() {
		return syncHandler(async(), RediSearchCommands.class, RedisClusterCommands.class);
	}

	/**
	 * Create a new instance of {@link RediSearchAsyncCommandsImpl}. Can be overriden to
	 * extend.
	 *
	 * @return a new instance
	 */
	protected RediSearchAsyncCommandsImpl<K, V> newRediSearchAsyncCommandsImpl() {
		return new RediSearchAsyncCommandsImpl<>(this, codec);
	}

	@Override
	public RediSearchReactiveCommands<K, V> reactive() {
		return reactive;
	}

	/**
	 * Create a new instance of {@link RediSearchReactiveCommandsImpl}. Can be overriden
	 * to extend.
	 *
	 * @return a new instance
	 */
	protected RediSearchReactiveCommandsImpl<K, V> newRediSearchReactiveCommandsImpl() {
		return new RediSearchReactiveCommandsImpl<>(this, codec);
	}

	@Override
	public RediSearchCommands<K, V> sync() {
		return sync;
	}

	/**
	 * Add a new listener.
	 *
	 * @param listener Listener.
	 */
	@Override
	public void addListener(PushListener listener) {
		pushHandler.addListener(listener);
	}

	/**
	 * Remove an existing listener.
	 *
	 * @param listener Listener.
	 */
	@Override
	public void removeListener(PushListener listener) {
		pushHandler.removeListener(listener);
	}

	@Override
	public boolean isMulti() {
		return multi != null;
	}

	@Override
	public <T> RedisCommand<K, V, T> dispatch(RedisCommand<K, V, T> command) {

		RedisCommand<K, V, T> toSend = preProcessCommand(command);

		try {
			return super.dispatch(toSend);
		} finally {
			if (command.getType().name().equals(MULTI.name())) {
				multi = (multi == null ? new MultiOutput<>(codec) : multi);
			}
		}
	}

	@Override
	public Collection<RedisCommand<K, V, ?>> dispatch(Collection<? extends RedisCommand<K, V, ?>> commands) {

		List<RedisCommand<K, V, ?>> sentCommands = new ArrayList<>(commands.size());

		commands.forEach(o -> {
			RedisCommand<K, V, ?> command = preProcessCommand(o);

			sentCommands.add(command);
			if (command.getType().name().equals(MULTI.name())) {
				multi = (multi == null ? new MultiOutput<>(codec) : multi);
			}
		});

		return super.dispatch(sentCommands);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <T> RedisCommand<K, V, T> preProcessCommand(RedisCommand<K, V, T> command) {

		RedisCommand<K, V, T> local = command;

		if (local.getType().name().equals(AUTH.name())) {
			local = attachOnComplete(local, status -> {
				if ("OK".equals(status)) {

					List<char[]> args = CommandArgsAccessor.getCharArrayArguments(command.getArgs());

					if (!args.isEmpty()) {
						state.setUserNamePassword(args);
					} else {

						List<String> strings = CommandArgsAccessor.getStringArguments(command.getArgs());
						state.setUserNamePassword(
								strings.stream().map(String::toCharArray).collect(Collectors.toList()));
					}
				}
			});
		}

		if (local.getType().name().equals(SELECT.name())) {
			local = attachOnComplete(local, status -> {
				if ("OK".equals(status)) {
					Long db = CommandArgsAccessor.getFirstInteger(command.getArgs());
					if (db != null) {
						state.setDb(db.intValue());
					}
				}
			});
		}

		if (local.getType().name().equals(READONLY.name())) {
			local = attachOnComplete(local, status -> {
				if ("OK".equals(status)) {
					state.setReadOnly(true);
				}
			});
		}

		if (local.getType().name().equals(READWRITE.name())) {
			local = attachOnComplete(local, status -> {
				if ("OK".equals(status)) {
					state.setReadOnly(false);
				}
			});
		}

		if (local.getType().name().equals(DISCARD.name())) {
			if (multi != null) {
				multi.cancel();
				multi = null;
			}
		}

		if (local.getType().name().equals(EXEC.name())) {
			MultiOutput<K, V> multiOutput = this.multi;
			this.multi = null;
			if (multiOutput == null) {
				multiOutput = new MultiOutput<>(codec);
			}
			local.setOutput((MultiOutput) multiOutput);
		}

		if (multi != null && !local.getType().name().equals(MULTI.name())) {
			local = new TransactionalCommand<>(local);
			multi.add(local);
		}
		return local;
	}

	@SuppressWarnings("unchecked")
	private <T> RedisCommand<K, V, T> attachOnComplete(RedisCommand<K, V, T> command, Consumer<T> consumer) {

		if (command instanceof CompleteableCommand) {
			CompleteableCommand<T> completeable = (CompleteableCommand<T>) command;
			completeable.onComplete(consumer);
		}
		return command;
	}

	/**
	 * @param clientName name to assign to this connection
	 * @deprecated since 6.0, use {@link RediSearchAsyncCommands#clientSetname(Object)}.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Deprecated
	public void setClientName(String clientName) {

		CommandArgs<String, String> args = new CommandArgs<>(StringCodec.UTF8).add(CommandKeyword.SETNAME)
				.addValue(clientName);
		AsyncCommand<String, String, String> async = new AsyncCommand<>(
				new Command<>(CommandType.CLIENT, new StatusOutput<>(StringCodec.UTF8), args));
		state.setClientName(clientName);

		dispatch((RedisCommand) async);
	}

	public ConnectionState getConnectionState() {
		return state;
	}

}
