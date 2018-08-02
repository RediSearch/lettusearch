# LettuSearch
[RediSearch](https://oss.redislabs.com/redisearch) client based on [Lettuce](https://lettuce.io)

## Architecture
LettuSearch implements RediSearch commands using the [Redis Command Interface abstraction](https://lettuce.io/core/5.0.1.RELEASE/reference/#redis-command-interfaces) provided by Lettuce.

## Usage
```java
RediSearchConnection<String, String> conn = RediSearchClient.create("redis://localhost").connect();
RediSearchCommands<String, String> commands = conn.search();
```