package xingu.maze;

import static org.junit.Assert.*;

import org.junit.Test;

import xingu.maze.Args;

public class ArgsTest
{
	@Test
	public void testEmptyArgs()
		throws Exception
	{
		Args args = new Args(new String[]{});
		assertEquals(null, args.get("p"));
		assertEquals(false, args.has("p"));
	}

	@Test
	public void testArgs()
		throws Exception
	{
		Args args = new Args(new String[]{"-a", "-b=B"});
		assertEquals(null, args.get("p"));
		assertEquals(false, args.has("p"));
		
		assertEquals("a", args.get("a"));
		assertEquals(true, args.has("a"));

		assertEquals("B", args.get("b"));
		assertEquals(true, args.has("b"));
	}

	@Test
	public void testOrder()
		throws Exception
	{
		Args args = new Args(new String[]{"-ax=B", "-a"});
		
		assertEquals("a", args.get("a"));
		assertEquals(true, args.has("a"));

		assertEquals("B", args.get("ax"));
		assertEquals(true, args.has("ax"));
	}

	@Test
	public void testMissingValue()
		throws Exception
	{
		Args args = new Args(new String[]{"-a="});
		assertEquals("", args.get("a"));
		assertEquals(true, args.has("a"));
	}

}
