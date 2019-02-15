# LettuSearch
Java client for [RediSearch](https://redisearch.io) based on [Lettuce](https://lettuce.io)

## Architecture
LettuSearch implements RediSearch commands using the [Command abstraction](https://lettuce.io/core/5.0.1.RELEASE/reference/#_custom_commands) provided by Lettuce.

## Building
```
mvn clean install
```

## Usage
Add LettuSearch to your application dependencies, e.g. with Maven:
```
<dependency>
	<groupId>com.redislabs</groupId>
	<artifactId>lettusearch</artifactId>
	<version>1.0.3</version>
</dependency>
```

```java
StatefulRediSearchConnection<String, String> conn = RediSearchClient.create("redis://localhost").connect();
RediSearchCommands<String, String> commands = conn.sync();
...
```
