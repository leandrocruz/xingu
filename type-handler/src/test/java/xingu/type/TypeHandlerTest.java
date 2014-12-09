package xingu.type;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import xingu.type.TypeHandler;
import xingu.type.impl.BooleanTypeHandler;
import xingu.type.impl.ByteTypeHandler;
import xingu.type.impl.CharTypeHandler;
import xingu.type.impl.DateTypeHandler;
import xingu.type.impl.DoubleTypeHandler;
import xingu.type.impl.FloatTypeHandler;
import xingu.type.impl.IntegerTypeHandler;
import xingu.type.impl.LongTypeHandler;
import xingu.type.impl.StringTypeHandler;

public class TypeHandlerTest
{
	@Test
	public void testBooleanTypeHandler()
		throws Exception
	{
		TypeHandler handler = new BooleanTypeHandler();
		Object      object  = handler.toObject("true");
		assertEquals(true, object);
		
		String string = handler.toString(true);
		assertEquals(string, "true");
	}

	@Test
	public void testByteTypeHandler()
		throws Exception
	{
		TypeHandler handler = new ByteTypeHandler();
		byte		value	= 0;
		Object      object  = handler.toObject("0");
		assertEquals(value, object);
		
		String string = handler.toString(value);
		assertEquals(string, "0");
	}

	@Test
	public void testCharTypeHandler()
		throws Exception
	{
		TypeHandler handler = new CharTypeHandler();
		Object      object  = handler.toObject("c");
		assertEquals('c', object);
		
		String string = handler.toString('c');
		assertEquals(string, "c");
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
		TypeHandler handler = new LongTypeHandler();
		long	    value   = 12;
		Object      object  = handler.toObject("12");
		assertEquals(value, object);
		
		String string = handler.toString(value);
		assertEquals("12", string);
	}

	@Test
	public void testDoubleTypeHandler()
		throws Exception
	{
		TypeHandler handler = new DoubleTypeHandler();
		double		value	= 9;
		Object      object  = handler.toObject("9");
		assertEquals(value, object);
		
		String string = handler.toString(value);
		assertEquals(string, "9.0");
	}
	
	@Test
	public void testFloatTypeHandler()
		throws Exception
	{
		TypeHandler handler = new FloatTypeHandler();
		float		value	= 10;
		Object      object  = handler.toObject("10");
		assertEquals(value, object);
		
		String string = handler.toString(value);
		assertEquals(string, "10.0");
	}

	@Test
	public void testDateTypeHandler()
		throws Exception
	{
		TypeHandler handler = new DateTypeHandler();
		Object      object  = handler.toObject("100");
		assertEquals(new Date(100), object);
		
		String string = handler.toString(new Date(13));
		assertEquals("13", string);
	}
}
