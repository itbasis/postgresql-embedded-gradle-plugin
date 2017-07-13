package ru.itbasis.gradle.plugins.postgresql

import de.flapdoodle.embed.process.config.IRuntimeConfig
import de.flapdoodle.embed.process.distribution.IVersion
import ru.yandex.qatools.embed.postgresql.distribution.Version

import static ru.yandex.qatools.embed.postgresql.EmbeddedPostgres.*

class PostgresqlExtension {
	public IVersion version = Version.Main.PRODUCTION
	public String host = DEFAULT_HOST
	public int port = 0
	public String dbName = DEFAULT_DB_NAME
	public String user = DEFAULT_USER
	public String password = DEFAULT_PASSWORD

	public List<String> addParams = new ArrayList<>()

	public IRuntimeConfig runtimeConfig = defaultRuntimeConfig()
}
