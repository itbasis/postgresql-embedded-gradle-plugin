package ru.itbasis.gradle.plugins.postgresql

import org.gradle.internal.impldep.com.google.common.io.Files
import org.gradle.testkit.runner.GradleRunner
import org.junit.*
import org.junit.rules.TemporaryFolder
import ru.yandex.qatools.embed.postgresql.distribution.Version

class PostgresqlEmbeddedPluginTest {
	@Rule
	public final TemporaryFolder testProjectDir = new TemporaryFolder()
	private GradleRunner gradleRunner

	@Before
	void setUp() throws Exception {
		gradleRunner = GradleRunner.create()
		                           .withDebug(true)
		                           .withProjectDir(testProjectDir.root)
		                           .forwardOutput()
		                           .withPluginClasspath()
	}

	private void initProjectStructure(String sourceBuildFileName, String javaTestClassName) {
		// build.gradle
		Files.copy(new File(this.class.classLoader.getResource(sourceBuildFileName + '.gradle').toURI()),
		           testProjectDir.newFile('build.gradle'))
		// main sources
		new File(testProjectDir.root, 'src/main/java').mkdirs()
		new File(testProjectDir.root, 'src/main/resources').mkdirs()
		// test sources
		final dirTestJava = new File(testProjectDir.root, 'src/test/java/test')
		dirTestJava.mkdirs()
		Files.copy(new File(this.class.classLoader.getResource(javaTestClassName + '.java').toURI()),
		           new File(dirTestJava, 'TestClass.java'))
	}

	@Test
	void blank() throws Exception {
		initProjectStructure('blank', 'TestClass')
		final result = gradleRunner.withArguments('tasks')
		                           .build()
		Assert.assertTrue(result.output
		                        .contains('EmbeddedPostgreSQL tasks\n'
			                                  + '------------------------\n'
			                                  + 'postgresqlServerStart\n'
			                                  + 'postgresqlServerStop\n'
		))
	}

	@Test
	void fail() throws Exception {
		initProjectStructure('blank', 'TestClass')
		final result = gradleRunner.withArguments('test')
		                           .buildAndFail()
		Assert.assertTrue(result.output.contains('testEnvironment FAILED'))
	}

	@Test
	void notSetEnvironment() throws Exception {
		initProjectStructure('blank', 'TestClassNotFound')
		final result = gradleRunner.withArguments('test')
		                           .build()
		Assert.assertFalse(result.output.contains(':postgresqlServerStart'))
	}

	@Test
	void version_Default() throws Exception {
		initProjectStructure('version-default', 'TestClass')
		final result = gradleRunner.withArguments('test')
		                           .build()
		Assert.assertTrue(result.output.contains(Version.Main.PRODUCTION.asInDownloadPath()))
	}

	/** TODO https://github.com/yandex-qatools/postgresql-embedded/issues/90 */
	@Test
	@Ignore
	void version_9_5() throws Exception {
		initProjectStructure('version-9.5', 'TestClass')
		final result = gradleRunner.withArguments('test')
		                           .build()
		Assert.assertTrue(result.output.contains(Version.Main.V9_5.asInDownloadPath()))
	}

	/** TODO https://github.com/yandex-qatools/postgresql-embedded/issues/90 */
	@Test
	@Ignore
	void version_9_6() throws Exception {
		initProjectStructure('version-9.6', 'TestClass')
		final result = gradleRunner.withArguments('test')
		                           .build()
		Assert.assertTrue(result.output.contains(Version.Main.V9_6.asInDownloadPath()))
	}
}
