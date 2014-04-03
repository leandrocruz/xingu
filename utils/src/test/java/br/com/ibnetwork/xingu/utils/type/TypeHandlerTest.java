package br.com.ibnetwork.xingu.utils.type;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.type.impl.IntegerTypeHandler;
import br.com.ibnetwork.xingu.utils.type.impl.StringTypeHandler;

public class TypeHandlerTest
{
	@Test
	public void testBooleanTypeHandler()
		throws Exception
	{
		throw new NotImplementedYet();
	}

	@Test
	public void testByteTypeHandler()
		throws Exception
	{
		throw new NotImplementedYet();
	}

	@Test
	public void testCharTypeHandler()
		throws Exception
	{
		throw new NotImplementedYet();
	}

	@Test
	public void testStringTypeHandler()
		throws Exception
	{
		TypeHandler handler = new StringTypeHandler();
		String      input   = "my little string";
		Object      object  = handler.toObject(input);
		assertEquals(input, object);
		
		String string = handler.toString(input);
		assertEquals(string, input);
	}

	@Test
	public void testIntegerTypeHandler()
		throws Exception
	{
		TypeHandler handler = new IntegerTypeHandler();
		Object      object  = handler.toObject("1");
		assertEquals(1, object);
		
		String string = handler.toString(99);
		assertEquals("99", string);
	}

	@Test(expected=NumberFormatException.class)
	public void testEmptyInputAtIntegerTypeHandler()
		throws Exception
	{
		new IntegerTypeHandler().toObject("");
	}
	
	@Test
	public void testLongTypeHandler()
		throws Exception
	{
		throw new NotImplementedYet();
	}

	@Test
	public void testDoubleTypeHandler()
		throws Exception
	{
		throw new NotImplementedYet();
	}
	
	@Test
	public void testFloatTypeHandler()
		throws Exception
	{
		throw new NotImplementedYet();
	}

	@Test
	public void testDateTypeHandler()
		throws Exception
	{
		throw new NotImplementedYet();
	}
}
