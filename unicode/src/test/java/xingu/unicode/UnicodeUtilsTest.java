package xingu.unicode;

import org.junit.Test;

public class UnicodeUtilsTest
{
	@Test
	public void testSanitize()
	{
		UnicodeUtils.sanitize("ções");
	}
}
