package xingu.journal;

import static org.junit.Assert.assertEquals;


import org.junit.Test;

public class PatternInterpolatorTest
{
	@Test
	public void testInterpolate()
		throws Exception
	{
		assertEquals("1", 	PatternInterpolator.interpolate("{}", 1));
		assertEquals(" 1", 	PatternInterpolator.interpolate(" {}", 1));
		assertEquals(" 1 ", PatternInterpolator.interpolate(" {} ", 1));
		assertEquals("1 ", 	PatternInterpolator.interpolate("{} ", 1));

		assertEquals("12", 		PatternInterpolator.interpolate("{}{}", 1, 2));
		assertEquals("1 2", 	PatternInterpolator.interpolate("{} {}", 1, 2));
		assertEquals(" 1 2", 	PatternInterpolator.interpolate(" {} {}", 1, 2));
		assertEquals(" 1 2 ", 	PatternInterpolator.interpolate(" {} {} ", 1, 2));
		assertEquals(" 12 ", 	PatternInterpolator.interpolate(" {}{} ", 1, 2));
		assertEquals(" 12", 	PatternInterpolator.interpolate(" {}{}", 1, 2));
		assertEquals("12 ", 	PatternInterpolator.interpolate("{}{} ", 1, 2));
		
		assertEquals("I wanna rock", 										PatternInterpolator.interpolate("I wanna {}", "rock"));
		assertEquals("I wanna aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 	PatternInterpolator.interpolate("I wanna {}", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
	}
}
