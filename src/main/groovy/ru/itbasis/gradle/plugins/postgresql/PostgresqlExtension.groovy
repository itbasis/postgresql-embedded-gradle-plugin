package ru.itbasis.gradle.plugins.postgresql

class PostgresqlExtension {
	public Map<String, EnvironmentConfig> environment = [
		'default': new EnvironmentConfig()
	]
}
