package br.com.ibnetwork.xingu.utils.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import br.com.ibnetwork.xingu.utils.clone.SimpleObject;
import br.com.ibnetwork.xingu.utils.clone.WithCollection;


public class ObjectVisitorTest
{
	@Test
	public void testVisit()
		throws Exception
	{
		new SimpleObjectVisitor().visit(new SimpleObject(1, "a string", null));
	}

	@Test
	public void testVisitArray()
		throws Exception
	{
		String[] array = new String[]{"a1", "a2"};
		new SimpleObjectVisitor().visit(array);
	}
	
	@Test
	public void testVisitObjectWithCollection()
		throws Exception
	{
		List<SimpleObject> list = new ArrayList<SimpleObject>();
		list.add(new SimpleObject(1, "One", "Eins"));
		list.add(new SimpleObject(2, "Two", "Zwei"));
		WithCollection obj = new WithCollection(list);
		new SimpleObjectVisitor().visit(obj);
	}
	
	@Test
	public void testTreeSet()
		throws Exception
	{
		Set<String> obj = new TreeSet<String>();
		new SimpleObjectVisitor().visit(obj);
	}

}
