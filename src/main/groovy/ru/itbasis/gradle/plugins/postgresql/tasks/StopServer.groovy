package ru.itbasis.gradle.plugins.postgresql.tasks

import groovy.util.logging.Slf4j
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.itbasis.gradle.plugins.postgresql.PostgresqlEmbeddedPlugin

@Slf4j
class StopServer extends DefaultTask {
	@TaskAction
	void action() {
		final postgresServer = PostgresqlEmbeddedPlugin.postgresServer
		if (postgresServer != null && postgresServer.process.present) {
			postgresServer.stop()
		}
	}
}
