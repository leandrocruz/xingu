package xingu.utils.lang;

import static org.junit.Assert.*;

import org.junit.Test;

import xingu.lang.Sys;

public class SysTest {

	@Test
	public void testIsntWindows() {
		boolean expectedValue = System.getProperty("os.name").toLowerCase().contains("windows");
		boolean retrievedValue = Sys.isWindows();
		assertEquals(expectedValue, retrievedValue);
	}
}
