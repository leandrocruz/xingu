package br.com.ibnetwork.xingu.utils.inspector;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import br.com.ibnetwork.xingu.utils.ArrayUtils;
import br.com.ibnetwork.xingu.utils.classloader.impl.ContextClassLoaderManager;
import br.com.ibnetwork.xingu.utils.inspector.impl.SimpleObjectInspector;
import br.com.ibnetwork.xingu.utils.inspector.impl.XmlEmitter;
import br.com.ibnetwork.xingu.utils.inspector.impl.XmlReader;
import br.com.ibnetwork.xingu.utils.type.ObjectType.Type;
import br.com.ibnetwork.xingu.utils.type.TypeHandlerRegistry;
import br.com.ibnetwork.xingu.utils.type.impl.GenericTypeHandler;
import br.com.ibnetwork.xingu.utils.type.impl.TypeHandlerRegistryImpl;

public class ObjectInspectorTest
{
	@Test
	public void testPrimitives()
		throws Exception
	{
		execWith("a string");
		execWith("html&nbsp;entities");
		execWith('c');
		execWith(1);
		execWith((byte)0);
		execWith(10l);
		execWith(20d);
		execWith(30f);
		execWith(false);
	}

	@Test
	public void testTransient()
		throws Exception
	{
		WithTransient       obj      = new WithTransient();
		XmlEmitter          visitor  = new XmlEmitter();
		TypeHandlerRegistry registry = new TypeHandlerRegistryImpl();
		new SimpleObjectInspector(obj, registry).visit(visitor);
		String result   = visitor.getResult();
		String expected = "<obj id=\"1\" class=\"br.com.ibnetwork.xingu.utils.inspector.WithTransient\">\n</obj>\n";
		assertEquals(expected, result);
	}
	
	@Test
	public void testDate()
		throws Exception
	{
		execWith(new Date());
	}
	
	@Test
	public void testSimpleObject()
		throws Exception
	{
		execWith(new SimpleObject(1, "single"));
	}

	@Test
	public void testArrayOfSimpleObject()
		throws Exception
	{
		SimpleObject[] array = new SimpleObject[]{
				new SimpleObject(1, "array a"),
				new SimpleObject(2, "array b")
		};

		execWith(array);
	}

	@Test
	public void testListOfSimpleObject()
		throws Exception
	{
		SimpleObject[] array = new SimpleObject[]{
				new SimpleObject(1, "list a"),
				new SimpleObject(2, "list b")
		};
		
		List<SimpleObject> list = new ArrayList<SimpleObject>();
		for(SimpleObject simple : array)
		{
			list.add(simple);
		}
		
		execWith(list);
	}

	@Test
	public void testMapOfSimpleObject()
		throws Exception
	{
		Map<String, SimpleObject> map = new HashMap<String, SimpleObject>();
		execWith(map);
		
		map.put("a", new SimpleObject(1, "1"));
		execWith(map);
	}

	@Test
	public void testNestedObject()
		throws Exception
	{
		NestedObject obj = new NestedObject(1, new NestedObject(2));
		execWith(obj);
	}

	@Test
	public void testMap()
		throws Exception
	{
		Map<String, SimpleObject> map = new HashMap<String, SimpleObject>();
		execWith(new WithMap(map));
		
		map.put("a", new SimpleObject(1, "aa"));
		map.put("b", new SimpleObject(2, "bb"));
		
		execWith(new WithMap(map));
	}
	
	@Test
	public void testCyclicGraph()
		throws Exception
	{
		NestedObject obj = new NestedObject(1);
		obj.me = obj;
		execWith(obj);
	}

	@Test
	public void testUnregisteredClass()
		throws Exception
	{
		String xml = "<obj id=\"1'\" class=\"br.com.ibnetwork.xingu.utils.inspector.UnregisteredObject\"></obj>";

		Object decoded = decode(xml, new TypeHandlerRegistryImpl());
		UnregisteredObject unregistered = (UnregisteredObject) decoded;
		assertEquals(0, unregistered.anInt);
		assertEquals(null, unregistered.aString);
		assertEquals(null, unregistered.bString);

		xml = "<obj id=\"1'\" class=\"br.com.ibnetwork.xingu.utils.inspector.UnregisteredObject\">"
				+ "<int id=\"2\" field=\"anInt\" value=\"100\"/>"
				+ "<string id=\"3\" field=\"aString\" value=\"aaa\"/>"
				+ "</obj>";

		decoded = decode(xml, new TypeHandlerRegistryImpl());
		unregistered = (UnregisteredObject) decoded;
		assertEquals(100, unregistered.anInt);
		assertEquals("aaa", unregistered.aString);
		assertEquals(null, unregistered.bString);
	}
	
	@Test
	@SuppressWarnings("rawtypes")
	public void testEmptyCollection()
		throws Exception
	{
		String xml     = "<collection id=\"1\" class=\"java.util.ArrayList\"></collection>";
		Object decoded = decode(xml, new TypeHandlerRegistryImpl());
		List   list    = (List) decoded;
		assertEquals(0, list.size());
	}

	@Test
	@Ignore
	public void testEmptyArray()
		throws Exception
	{
		String   xml     = "<array id=\"1\" class=\"string[]\"></array>";
		Object   decoded = decode(xml, new TypeHandlerRegistryImpl());
		String[] array   = (String[]) decoded;
		assertEquals(0, array.length);
	}
	
	private String execWith(Object obj, TypeHandlerRegistry aliases)
		throws Exception
	{
		XmlEmitter visitor = new XmlEmitter();
		new SimpleObjectInspector(obj, aliases).visit(visitor);
		String result = visitor.getResult();
		System.err.println(result + "--");
		return result;
	}
	
	private String execWith(Object obj)
		throws Exception
	{
		TypeHandlerRegistry registry = new TypeHandlerRegistryImpl();
		registry.register(new GenericTypeHandler(SimpleObject.class, "simple", Type.OBJECT));
		registry.register(new GenericTypeHandler(NestedObject.class, "nested", Type.OBJECT));
		String encoded = execWith(obj, registry);
		Object decoded = decode(encoded, registry);
		
		if(obj.getClass().isArray())
		{
			assertEquals(true, ArrayUtils.equals((Object[])obj, (Object[])decoded));
		}
		else
		{
			assertEquals(obj, decoded);
		}
		
		return encoded;
	}

	private Object decode(String encoded, TypeHandlerRegistry registry)
		throws Exception
	{
		ObjectEmitter deserializer = new XmlReader(registry, ContextClassLoaderManager.getClassLoaderManager());
		return deserializer.from(encoded);
	}
}
