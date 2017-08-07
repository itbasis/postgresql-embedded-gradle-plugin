package ru.itbasis.gradle.plugins.postgresql

import org.gradle.api.Plugin
import org.gradle.api.internal.project.ProjectInternal
import ru.itbasis.gradle.plugins.postgresql.tasks.StartServer
import ru.itbasis.gradle.plugins.postgresql.tasks.StopServer
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres

class PostgresqlEmbeddedPlugin implements Plugin<ProjectInternal> {
	public static final String GROUP = 'embeddedPostgreSQL'
	public static final String TASK_START = 'postgresqlServerStart'
	public static final String TASK_STOP = 'postgresqlServerStop'

	public static EmbeddedPostgres postgresServer = null

	@Override
	void apply(ProjectInternal project) {
		project.extensions.create(GROUP, PostgresqlExtension.class)

		final taskStartServer = project.tasks.maybeCreate(TASK_START, StartServer.class)
		taskStartServer.group = GROUP

		final taskStopServer = project.tasks.maybeCreate(TASK_STOP, StopServer.class)
		taskStopServer.group = GROUP

		taskStartServer.finalizedBy(taskStopServer)
		taskStopServer.dependsOn.add(taskStartServer)
	}

}
