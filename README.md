# PostgreSQL Embedded Server Gradle plugin
[![Build status](https://travis-ci.org/itbasis/postgresql-embedded-gradle-plugin.svg?branch=master)](https://travis-ci.org/itbasis/postgresql-embedded-gradle-plugin)


A plugin that adds the ability to start/stop [Embedded PostgreSQL Server](https://github.com/yandex-qatools/postgresql-embedded) while executing Gradle tasks.
After the server is started, environment variables are added to all dependent tasks of the [JavaForkOptions](https://docs.gradle.org/current/javadoc/org/gradle/process/JavaForkOptions.html) type with the description of the running server.

apply plugin: https://plugins.gradle.org/plugin/ru.itbasis.gradle.plugins.postgresql-embedded-gradle-plugin

## Environment & System environment

[Java example](src/test/resources/TestClass.java), 
[Spring properties example](src/test/resources/spring-boot/src/main/resources/config/application.properties)

|default names|
|---|
|POSTGRES_HOST
|POSTGRES_PORT
|POSTGRES_DB_NAME
|POSTGRES_USER
|POSTGRES_PASSWORD
|POSTGRES_JDBC

## Extension properties

[source](src/main/groovy/ru/itbasis/gradle/plugins/postgresql/PostgresqlExtension.groovy)

|parameter|default value|
|---|---|
|version|PRODUCTION
|host|localhost
|port|0 _(find free port)_|
|dbName|postgres
|user|postgres
|password|postgres
|addParams|_(empty list)_
|runtimeConfig| [_view source code_](src/main/groovy/ru/itbasis/gradle/plugins/postgresql/EnvironmentConfig.groovy) |
|environmentNames|[_view source code_](src/main/groovy/ru/itbasis/gradle/plugins/postgresql/EnvironmentConfig.groovy)


## Examples

* depends tasks and default PostgreSql server version: [minimal](src/test/resources/minimal.gradle)
* custom PostgreSql server version: [9.5](src/test/resources/version-9.5.gradle), [9.6](src/test/resources/version-9.6.gradle)
* custom environment: [environment names](src/test/resources/custom-environment-names.gradle), [custom environment](src/test/resources/environment-custom-partial.gradle)
* [Spring Boot](src/test/resources/spring-boot)


## TODO

* append multiple instance