# LettuSearch
[RediSearch](https://redisearch.io) client based on [Lettuce](https://lettuce.io)

## Architecture
LettuSearch implements RediSearch commands using the [Command abstraction](https://lettuce.io/core/5.0.1.RELEASE/reference/#_custom_commands) provided by Lettuce.

## Building
```
mvn clean install
```

## Usage
```java
StatefulRediSearchConnection<String, String> conn = RediSearchClient.create("redis://localhost").connect();
RediSearchCommands<String, String> commands = conn.sync();
...
```
