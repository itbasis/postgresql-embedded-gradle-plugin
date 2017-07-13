# PostgreSQL Embedded Server Gradle plugin

A plugin that adds the ability to start/stop [Embedded PostgreSQL Server](https://github.com/yandex-qatools/postgresql-embedded) while executing Gradle tasks.
After the server is started, environment variables are added to all dependent tasks of the [JavaForkOptions](https://docs.gradle.org/current/javadoc/org/gradle/process/JavaForkOptions.html) type with the description of the running server.

apply plugin: https://plugins.gradle.org/plugin/ru.itbasis.gradle.plugins.postgresql-embedded-plugin

## Environment

[demo](src/test/resources/TestClass.java)

|name|
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


## Examples

* depends tasks and default PostgreSql server version: [minimal](src/test/resources/version-default.gradle)
* custom PostgreSql server version: [9.5](src/test/resources/version-9.5.gradle), [9.6](src/test/resources/version-9.6.gradle)


## TODO

* append multiple instance