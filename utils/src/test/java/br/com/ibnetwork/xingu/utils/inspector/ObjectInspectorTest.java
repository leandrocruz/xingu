package br.com.ibnetwork.xingu.utils.inspector;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.com.ibnetwork.xingu.utils.inspector.impl.SimpleObjectInspector;
import br.com.ibnetwork.xingu.utils.inspector.impl.XmlEmitter;
import br.com.ibnetwork.xingu.utils.inspector.impl.XmlObjectEmitter;
import br.com.ibnetwork.xingu.utils.type.TypeHandlerRegistry;
import br.com.ibnetwork.xingu.utils.type.ObjectType.Type;
import br.com.ibnetwork.xingu.utils.type.impl.GenericTypeHandler;
import br.com.ibnetwork.xingu.utils.type.impl.TypeHandlerRegistryImpl;

public class ObjectInspectorTest
{
	@Test
	public void testPrimitives()
		throws Exception
	{
		execWith("a string");
		execWith(1);
		execWith(false);
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
	public void testNestedObject()
		throws Exception
	{
		NestedObject obj = new NestedObject(1, new NestedObject(2));
		execWith(obj);
	}

	@Test
	public void testCyclicGraph()
		throws Exception
	{
		NestedObject obj = new NestedObject(1);
		obj.me = obj;
		execWith(obj);
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
		
		assertEquals(obj, decoded);
		
		return encoded;
	}

	private Object decode(String encoded, TypeHandlerRegistry registry)
		throws Exception
	{
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		ObjectEmitter deserializer = new XmlObjectEmitter(registry, cl);
		return deserializer.from(encoded);
	}
}
