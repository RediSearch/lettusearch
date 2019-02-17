[![license](https://img.shields.io/github/license/RedisLabs/lettusearch.svg)](https://github.com/RedisLabs/lettusearch)
<!--[![CircleCI](https://circleci.com/gh/RediSearch/lettusearch/tree/master.svg?style=svg)](https://circleci.com/gh/RediSearch/lettusearch/tree/master)
[![GitHub issues](https://img.shields.io/github/release/RedisLabs/lettusearch.svg)](https://github.com/RedisLabs/lettusearch/releases/latest)-->
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.redislabs/lettusearch/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.redislabs/lettusearch)
[![Javadocs](https://www.javadoc.io/badge/com.redislabs/lettusearch.svg)](https://www.javadoc.io/doc/com.redislabs/lettusearch)
<!--[![Codecov](https://codecov.io/gh/RediSearch/lettusearch/branch/master/graph/badge.svg)](https://codecov.io/gh/RediSearch/lettusearch)-->

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
