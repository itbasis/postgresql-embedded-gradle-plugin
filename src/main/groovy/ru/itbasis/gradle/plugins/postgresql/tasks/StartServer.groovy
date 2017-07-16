package ru.itbasis.gradle.plugins.postgresql.tasks

import groovy.util.logging.Slf4j
import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.process.JavaForkOptions
import ru.itbasis.gradle.plugins.postgresql.EnvironmentConfig
import ru.itbasis.gradle.plugins.postgresql.PostgresqlEmbeddedPlugin
import ru.itbasis.gradle.plugins.postgresql.PostgresqlExtension
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres
import ru.yandex.qatools.embed.postgresql.util.SocketUtil

@Slf4j
class StartServer extends DefaultTask {
	@Input
	public String environmentGroup = 'default'

	@SuppressWarnings("GroovyAssignabilityCheck")
	@TaskAction
	void action() {
		final extension = project.extensions.getByType(PostgresqlExtension.class)
		final EnvironmentConfig environment = extension.environment[environmentGroup]

		final jdbcUrl = runServer(environment)

		final tasks = project.tasks
		                     .withType(JavaForkOptions.class)
		                     .findAll { ((Task) it).dependsOn.contains(PostgresqlEmbeddedPlugin.TASK_START) }

		tasks.each { task ->
			logger.debug('processing task: {}', task)
			task.environment(environment.environmentNames.host, environment.host)
			task.environment(environment.environmentNames.port, environment.port)
			task.environment(environment.environmentNames.dbName, environment.dbName)
			task.environment(environment.environmentNames.user, environment.user)
			task.environment(environment.environmentNames.password, environment.password)
			task.environment(environment.environmentNames.jdbc, jdbcUrl)
		}
	}

	/**
	 * @return {@link EmbeddedPostgres#start(...)}
	 */
	@SuppressWarnings("GroovyAssignabilityCheck")
	private String runServer(EnvironmentConfig environment) {
		PostgresqlEmbeddedPlugin.postgresServer = new EmbeddedPostgres(environment.version)
		final postgresServer = PostgresqlEmbeddedPlugin.postgresServer

		if (environment.port == 0) {
			environment.port = SocketUtil.findFreePort()
		}
		if (environment.addParams == null) {
			environment.addParams = new ArrayList<>()
		}

		return postgresServer.start(environment.runtimeConfig,
		                            environment.host,
		                            environment.port,
		                            environment.dbName,
		                            environment.user,
		                            environment.password,
		                            environment.addParams)
	}

}
