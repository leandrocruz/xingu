package br.com.ibnetwork.xingu.utils.type;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import br.com.ibnetwork.xingu.utils.type.impl.SimpleObjectPopulator;

public class ObjectPopulatorTest
{
	@Test
	public void testNullValues()
		throws Exception
	{
		ObjectPopulator populator     = SimpleObjectPopulator.instance();
		Map<String,     String>   map = new HashMap<String, String>();
		SimpleObject    obj           = new SimpleObject();

		populator.populate(obj, map);
		
		assertEquals(0, obj.getIntField());
		assertEquals(null, obj.getStringField());
	}

	@Test
	public void testPopulate()
		throws Exception
	{
		ObjectPopulator populator     = SimpleObjectPopulator.instance();
		Map<String,     String>   map = new HashMap<String, String>();
		SimpleObject    obj           = new SimpleObject();
		
		map.put("intField", "10");
		map.put("stringField", "Bafana Bafana");
		
		populator.populate(obj, map);
		
		assertEquals(10, obj.getIntField());
		assertEquals("Bafana Bafana", obj.getStringField());
	}

	@Test
	public void testConvertion()
		throws Exception
	{
		ObjectPopulator populator     = SimpleObjectPopulator.instance();
		Map<String,     String>   map = new HashMap<String, String>();
		WithParam       obj           = new WithParam();
		
		map.put("x", "10");
		map.put("y", "Bafana Bafana");
		
		populator.populate(obj, map);
		
		assertEquals(10, obj.getIntField());
		assertEquals("Bafana Bafana", obj.getStringField());
	}
}