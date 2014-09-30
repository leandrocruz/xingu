package br.com.ibnetwork.xingu.utils.type;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.ibnetwork.xingu.utils.collection.FluidMap;
import br.com.ibnetwork.xingu.utils.type.impl.SimpleObjectPopulator;

public class ObjectPopulatorTest
{
	@Test
	public void testNullValues()
		throws Exception
	{
		ObjectPopulator populator = SimpleObjectPopulator.instance();
		SimpleObject    obj       = new SimpleObject();

		populator.populate(obj, new FluidMap<String>());
		
		assertEquals(0, obj.getIntField());
		assertEquals(null, obj.getStringField());
		assertArrayEquals("array conversion failed", null, obj.getArrayOfStrings());
	}

	@Test
	public void testPopulate()
		throws Exception
	{
		ObjectPopulator populator = SimpleObjectPopulator.instance();
		SimpleObject    obj       = new SimpleObject();
		
		FluidMap<String> fluid = new FluidMap<String>()
			.add("intField", "10")
			.add("stringField", "Bafana Bafana")
			.add("arrayOfStrings", "a", "b");
		
		populator.populate(obj, fluid);
		
		assertEquals(10, obj.getIntField());
		assertEquals("Bafana Bafana", obj.getStringField());
		String[] arrayOfStrings = obj.getArrayOfStrings();
		assertArrayEquals("array conversion failed", new String[]{"a",  "b"}, arrayOfStrings);
	}

	@Test
	public void testConvertion()
		throws Exception
	{
		ObjectPopulator populator = SimpleObjectPopulator.instance();
		WithParam       obj       = new WithParam();
		
		FluidMap<String> fluid = new FluidMap<String>()
				.add("x", "10")
				.add("y", "Bafana Bafana");
		
		populator.populate(obj, fluid);
		
		assertEquals(10, obj.getIntField());
		assertEquals("Bafana Bafana", obj.getStringField());
	}
}