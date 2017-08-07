package test;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class TestClassNotFound {
	@Test
	public void testEnvironment() throws Exception {
		final Map<String, String> env = System.getenv();
		Assert.assertFalse(env.containsKey("POSTGRES_HOST"));
		Assert.assertFalse(env.containsKey("POSTGRES_PORT"));
		Assert.assertFalse(env.containsKey("POSTGRES_JDBC"));
		Assert.assertFalse(env.containsKey("POSTGRES_USER"));
		Assert.assertFalse(env.containsKey("POSTGRES_PASSWORD"));
		Assert.assertFalse(env.containsKey("POSTGRES_DATABASE"));
	}
}