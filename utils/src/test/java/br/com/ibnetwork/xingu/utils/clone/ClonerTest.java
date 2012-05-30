package br.com.ibnetwork.xingu.utils.clone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.com.ibnetwork.xingu.utils.clone.impl.SimpleCloner;

public class ClonerTest
{
	@Test
	public void testCloneString()
		throws Exception
	{
		Cloner cloner = new SimpleCloner();
		String s1 = "I am who I am";
		String s2 = cloner.deepClone(s1);
		assertNotSame(s1, s2);
		assertEquals(s1, s2);
	}

	@Test
	public void testCloneSimpleObject()
		throws Exception
	{
		Cloner cloner = new SimpleCloner();
		SimpleObject o1 = new SimpleObject(10, "Isaiah 61");
		SimpleObject o2 = cloner.deepClone(o1);
		assertNotSame(o1, o2);
		assertEquals(o1, o2);
	}

	@Test
	public void testCloneNestedObject()
		throws Exception
	{
		Cloner cloner = new SimpleCloner();
		SimpleObject f1 = new SimpleObject(10, "John 3:16");
		Nested o1 = new Nested(99, f1);
		Nested o2 = cloner.deepClone(o1);
		assertNotSame(o1, o2);
		assertEquals(o1, o2);
		
		SimpleObject f2 = o2.simple();
		assertNotSame(f1, f2);
		assertEquals(f1, f2);
	}

	@Test
	public void testCloneObjectWithTransient()
		throws Exception
	{
		Cloner cloner = new SimpleCloner();
		Transient o1 = new Transient(10, 20);
		Transient o2 = cloner.deepClone(o1);
		
		assertNotSame(o1, o2);
		assertEquals(10, o2.i1());
		assertEquals(0, o2.i2());
	}

	@Test
	public void testCloneObjectWithCollection()
		throws Exception
	{
		Cloner cloner = new SimpleCloner();

		List<SimpleObject> l1 = new ArrayList<SimpleObject>();
		l1.add(new SimpleObject(1, "One"));
		l1.add(new SimpleObject(2, "Two"));
		
		WithCollection o1 = new WithCollection(l1);
		WithCollection o2 = cloner.deepClone(o1);
		assertNotSame(o1, o2);
		
		List<SimpleObject> l2 = o2.list();
		assertNotSame(l1, l2);
		assertEquals(l1.size(), l2.size());
		
		int size = l1.size();
		for (int i = 0; i < size; i++)
		{
			SimpleObject s1 = l1.get(i);
			SimpleObject s2 = l2.get(i);
			assertNotSame(s1, s2);
			assertEquals(s1, s2);
		}
	}

	@Test
	public void testCloneObjectWithArray()
		throws Exception
	{
		Cloner cloner = new SimpleCloner();

		SimpleObject[] a1 = new SimpleObject[] {
				new SimpleObject(1, "One"),
				new SimpleObject(2, "Two")
		};
		
		WithArray o1 = new WithArray(a1);
		WithArray o2 = cloner.deepClone(o1);
		assertNotSame(o1, o2);
		
		SimpleObject[] a2 = o2.array();
		assertNotSame(a1, a2);
		assertEquals(a1.length, a2.length);
		
		int size = a1.length;
		for (int i = 0; i < size; i++)
		{
			SimpleObject s1 = a1[i];
			SimpleObject s2 = a2[i];
			assertNotSame(s1, s2);
			assertEquals(s1, s2);
		}
	}
}
