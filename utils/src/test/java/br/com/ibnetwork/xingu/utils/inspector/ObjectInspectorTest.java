package br.com.ibnetwork.xingu.utils.inspector;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.com.ibnetwork.xingu.utils.inspector.impl.XmlEmitter;
import br.com.ibnetwork.xingu.utils.inspector.impl.SimpleObjectInspector;

public class ObjectInspectorTest
{
	@Test
	public void testSimpleObject()
		throws Exception
	{
		new SimpleObjectInspector(new SimpleObject(1, "single")).visit(new XmlEmitter());
	}

	@Test
	public void testArrayOfSimpleObject()
		throws Exception
	{
		SimpleObject[] array = new SimpleObject[]{
				new SimpleObject(1, "array a"),
				new SimpleObject(2, "array b")
		};
		
		new SimpleObjectInspector(array).visit(new XmlEmitter());
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
		
		new SimpleObjectInspector(list).visit(new XmlEmitter());
	}
	
	@Test
	public void testNestedObject()
		throws Exception
	{
		NestedObject obj = new NestedObject(1, new NestedObject(2));
		new SimpleObjectInspector(obj).visit(new XmlEmitter());
	}
	
}
