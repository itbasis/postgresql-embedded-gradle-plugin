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

		runServer(environment)

		final tasks = project.tasks
		                     .withType(JavaForkOptions.class)
		                     .findAll { ((Task) it).dependsOn.contains(PostgresqlEmbeddedPlugin.TASK_START) }

		tasks.each { task ->
			logger.info('processing task: {}', task)
			['host', 'port', 'dbName', 'user', 'password', 'jdbc'].each { paramName ->
				task.systemProperty(environment.environmentNames[paramName], environment[paramName])
				task.environment(environment.environmentNames[paramName], environment[paramName])
			}
		}
	}

	/**
	 * @return {@link EmbeddedPostgres#start(...)}
	 */
	@SuppressWarnings("GroovyAssignabilityCheck")
	private static void runServer(EnvironmentConfig environment) {
		PostgresqlEmbeddedPlugin.postgresServer = new EmbeddedPostgres(environment.version)
		final postgresServer = PostgresqlEmbeddedPlugin.postgresServer

		if (environment.port == 0) {
			environment.port = SocketUtil.findFreePort()
		}
		if (environment.addParams == null) {
			environment.addParams = new ArrayList<>()
		}

		final jdbcUrl = postgresServer.start(environment.runtimeConfig,
		                                     environment.host,
		                                     environment.port,
		                                     environment.dbName,
		                                     environment.user,
		                                     environment.password,
		                                     environment.addParams)

		if (!(environment.jdbc?.trim())) {
			environment.jdbc = jdbcUrl
		}
	}

}
