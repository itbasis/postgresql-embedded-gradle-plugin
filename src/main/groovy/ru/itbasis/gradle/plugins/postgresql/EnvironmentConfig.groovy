package ru.itbasis.gradle.plugins.postgresql

import de.flapdoodle.embed.process.config.IRuntimeConfig
import de.flapdoodle.embed.process.distribution.IVersion
import ru.yandex.qatools.embed.postgresql.distribution.Version

import static ru.yandex.qatools.embed.postgresql.EmbeddedPostgres.*

class EnvironmentConfig {
	public IVersion version = Version.Main.PRODUCTION
	public String host = DEFAULT_HOST
	public int port = 0
	public String dbName = DEFAULT_DB_NAME
	public String user = DEFAULT_USER
	public String password = DEFAULT_PASSWORD
	public List<String> addParams = new ArrayList<>()

	public IRuntimeConfig runtimeConfig = defaultRuntimeConfig()

	public Map<String, String> environmentNames = [
		'host'    : 'POSTGRES_HOST',
		'port'    : 'POSTGRES_PORT',
		'dbName'  : 'POSTGRES_DB_NAME',
		'user'    : 'POSTGRES_USER',
		'password': 'POSTGRES_PASSWORD',
		'jdbc'    : 'POSTGRES_JDBC'
	]
}
