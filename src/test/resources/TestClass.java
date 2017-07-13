package test;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Map;

public class TestClass {
	@Test
	public void testEnvironment() throws Exception {
		final Map<String, String> env = System.getenv();
		Assert.assertTrue(env.containsKey("POSTGRES_HOST"));
		Assert.assertTrue(env.containsKey("POSTGRES_PORT"));
		Assert.assertTrue(env.containsKey("POSTGRES_DB_NAME"));
		Assert.assertTrue(env.containsKey("POSTGRES_USER"));
		Assert.assertTrue(env.containsKey("POSTGRES_PASSWORD"));
		Assert.assertTrue(env.containsKey("POSTGRES_JDBC"));
	}

	@Test
	public void testConnection() throws Exception {
		final String jdbcUrl = System.getenv("POSTGRES_JDBC");
		System.out.println("jdbcUrl: " + jdbcUrl);

		try (final Connection conn = DriverManager.getConnection(jdbcUrl)) {

			try (final Statement statement = conn.createStatement()) {
				Assert.assertTrue(statement.execute("SELECT version();"));
				Assert.assertTrue(statement.getResultSet().next());
				System.out.println(statement.getResultSet().getString("version"));
			}

			try (final Statement statement = conn.createStatement()) {
				statement.execute("CREATE TABLE films (code CHAR(5));");
				statement.execute("INSERT INTO films VALUES ('movie');");
			}

			try (final Statement statement = conn.createStatement()) {
				Assert.assertTrue(statement.execute("SELECT * FROM films;"));
				Assert.assertTrue(statement.getResultSet().next());
				Assert.assertEquals("movie", statement.getResultSet().getString("code"));
			}

		}
	}

}