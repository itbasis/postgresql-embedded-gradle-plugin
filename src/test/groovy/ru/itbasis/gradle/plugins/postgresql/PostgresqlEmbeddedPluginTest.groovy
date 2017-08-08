package ru.itbasis.gradle.plugins.postgresql

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import org.gradle.internal.impldep.com.google.common.io.Files
import org.gradle.testkit.runner.GradleRunner
import org.hamcrest.core.IsNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.yandex.qatools.embed.postgresql.distribution.Version

import static org.hamcrest.CoreMatchers.not
import static org.hamcrest.core.Is.is
import static org.hamcrest.core.StringContains.containsString
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder
import static org.junit.Assert.assertThat

class PostgresqlEmbeddedPluginTest {
	@Rule
	public final TemporaryFolder testProjectDir = new TemporaryFolder()
	private GradleRunner gradleRunner

	@Before
	void setUp() throws Exception {
		System.setProperty("jna.debug_load", "true")
		System.setProperty("jna.nosys", "true")
		gradleRunner = GradleRunner.create()
		                           .withDebug(true)
		                           .withProjectDir(testProjectDir.root)
		                           .forwardOutput()
		                           .withPluginClasspath()
	}

	private void initProjectStructure(String sourceBuildFileName, String... testSourcePaths) {
		// build.gradle
		Files.copy(new File(this.class.classLoader.getResource(sourceBuildFileName + '.gradle').file),
		           testProjectDir.newFile('build.gradle'))
		// main sources
		new File(testProjectDir.root, 'src/main/java').mkdirs()
		new File(testProjectDir.root, 'src/main/resources').mkdirs()
		// test sources
		final dirTestJava = new File(testProjectDir.root, 'src/test/java/test')
		dirTestJava.mkdirs()

		testSourcePaths.each { testSourcePath ->
			final resource = this.class.classLoader.getResource(testSourcePath)
			assertThat('get test source path: ' + testSourcePath, resource, IsNull.notNullValue())

			final testResource = new File(resource.file)
			assertThat(testResource.exists(), is(true))
			if (testResource.file) {
				FileUtils.copyFileToDirectory(testResource, dirTestJava)
			} else {
				FileUtils.copyDirectory(testResource, testProjectDir.root)
			}
		}
	}

	private static void assertOrderTasks(String output, String... tasks = ['test']) throws Exception {
		assertThat(StringUtils.countMatches(output, ':postgresqlServerStart'), is(1))
		assertThat(StringUtils.countMatches(output, ':postgresqlServerStop'), is(1))

		tasks.each { taskName ->
			assertThat(output, stringContainsInOrder([':postgresqlServerStart', taskName, ':postgresqlServerStop']))
		}
	}

	@Test
	void blank() throws Exception {
		initProjectStructure('blank', 'TestClass.java')
		final result = gradleRunner.withArguments('tasks')
		                           .build()
		assertThat(result.output, stringContainsInOrder(['EmbeddedPostgreSQL tasks',
		                                                 'postgresqlServerStart',
		                                                 'postgresqlServerStop']))
	}

	@Test
	void fail() throws Exception {
		initProjectStructure('blank', 'TestClass.java')
		final result = gradleRunner.withArguments('test')
		                           .buildAndFail()
		assertThat(result.output, containsString('testEnvironment FAILED'))
	}

	@Test
	void notSetEnvironment() throws Exception {
		initProjectStructure('blank', 'TestClassNotFound.java')
		final result = gradleRunner.withArguments('test')
		                           .build()
		assertThat(result.output, not(containsString(':postgresqlServerStart')))
	}

	@Test
	void minimal() throws Exception {
		initProjectStructure('minimal', 'TestClass.java')
		final result = gradleRunner.withArguments('test')
		                           .build()
		assertThat(result.output, containsString(Version.Main.PRODUCTION.asInDownloadPath()))
		assertOrderTasks(result.output)
	}

	@Test
	void version_9_5() throws Exception {
		initProjectStructure('version-9.5', 'TestClass.java')
		final result = gradleRunner.withArguments('test')
		                           .build()
		assertThat(result.output, containsString(Version.Main.V9_5.asInDownloadPath()))
		assertOrderTasks(result.output)
	}

	@Test
	void version_9_6() throws Exception {
		initProjectStructure('version-9.6', 'TestClass.java')
		final result = gradleRunner.withArguments('test')
		                           .build()
		assertThat(result.output, containsString(Version.Main.V9_6.asInDownloadPath()))
		assertOrderTasks(result.output)
	}

	@Test
	void customEnvironmentNames() throws Exception {
		initProjectStructure('custom-environment-names', 'TestClassCustomEnvironmentNames.java')
		final result = gradleRunner.withArguments('test')
		                           .build()
		assertThat(result.output, containsString(Version.Main.PRODUCTION.asInDownloadPath()))
		assertOrderTasks(result.output)
	}

	@Test
	void customEnvironmentPartial() throws Exception {
		initProjectStructure('environment-custom-partial', 'TestClass.java')
		final result = gradleRunner.withArguments('test')
		                           .build()
		assertThat(result.output, containsString(Version.Main.V9_5.asInDownloadPath()))
		assertOrderTasks(result.output)
	}

	@Test
	void springBootRun() throws Exception {
		initProjectStructure('spring-boot/build', 'spring-boot', 'TestClass.java')
		final result = gradleRunner.withArguments('test', 'bootRun')
		                           .build()
		assertThat(result.output, containsString(Version.Main.V9_5.asInDownloadPath()))
		assertOrderTasks(result.output, 'test', 'bootRun')
	}
}
