# PostgreSQL Embedded Server Gradle plugin

A plugin that adds the ability to start/stop [Embedded PostgreSQL Server](https://github.com/yandex-qatools/postgresql-embedded) while executing Gradle tasks.
After the server is started, environment variables are added to all dependent tasks of the [JavaForkOptions](https://docs.gradle.org/current/javadoc/org/gradle/process/JavaForkOptions.html) type with the description of the running server.

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

|parameter|default value|
|---|---|
|version|[PRODUCTION](https://github.com/yandex-qatools/postgresql-embedded/blob/master/src/main/java/ru/yandex/qatools/embed/postgresql/distribution/Version.java)
|host|[localhost](https://github.com/yandex-qatools/postgresql-embedded/blob/master/src/main/java/ru/yandex/qatools/embed/postgresql/EmbeddedPostgres.java)
|port|0 _(find free port)_|
|dbName|[postgres](https://github.com/yandex-qatools/postgresql-embedded/blob/master/src/main/java/ru/yandex/qatools/embed/postgresql/EmbeddedPostgres.java)
|user|[postgres](https://github.com/yandex-qatools/postgresql-embedded/blob/master/src/main/java/ru/yandex/qatools/embed/postgresql/EmbeddedPostgres.java)
|password|[postgres](https://github.com/yandex-qatools/postgresql-embedded/blob/master/src/main/java/ru/yandex/qatools/embed/postgresql/EmbeddedPostgres.java)


## Examples

* depends tasks and default PostgreSql server version: [minimal](src/test/resources/version-default.gradle), [full](src/test/resources/version-default-full.gradle)
* custom PostgreSql server version: [9.5](src/test/resources/version-9.5.gradle), [9.6](src/test/resources/version-9.6.gradle)


## TODO

* append multiple instance