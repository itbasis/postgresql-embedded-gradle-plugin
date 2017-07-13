package ru.itbasis.gradle.plugins.postgresql.tasks

import groovy.util.logging.Slf4j
import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.tasks.TaskAction
import org.gradle.process.JavaForkOptions
import ru.itbasis.gradle.plugins.postgresql.PostgresqlEmbeddedPlugin
import ru.itbasis.gradle.plugins.postgresql.PostgresqlExtension
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres
import ru.yandex.qatools.embed.postgresql.util.SocketUtil

@Slf4j
class StartServer extends DefaultTask {
	@TaskAction
	void action() {
		final jdbcUrl = runServer()

		final tasks = project.tasks
		                     .withType(JavaForkOptions.class)
		                     .findAll { ((Task) it).dependsOn.contains(PostgresqlEmbeddedPlugin.TASK_START) }
		final extension = project.extensions.getByType(PostgresqlExtension.class)

		tasks.each { task ->
			logger.debug('processing task: {}', task)
			task.environment([
				'POSTGRES_HOST'      : extension.host
				, 'POSTGRES_PORT'    : extension.port
				, 'POSTGRES_DB_NAME' : extension.dbName
				, 'POSTGRES_USER'    : extension.user
				, 'POSTGRES_PASSWORD': extension.password
				, 'POSTGRES_JDBC'    : jdbcUrl
			])
		}
	}

	/**
	 * @return {@link EmbeddedPostgres#start(...)}
	 */
	private String runServer() {
		final PostgresqlExtension extension = project.extensions.getByType(PostgresqlExtension.class)
		PostgresqlEmbeddedPlugin.postgresServer = new EmbeddedPostgres(extension.version)
		final postgresServer = PostgresqlEmbeddedPlugin.postgresServer

		if (extension.port == 0) {
			extension.port = SocketUtil.findFreePort()
		}
		if (extension.addParams == null) {
			extension.addParams = new ArrayList<>()
		}

		return postgresServer.start(extension.runtimeConfig, extension.host, extension.port, extension.dbName,
		                            extension.user, extension.password, extension.addParams)
	}
}
