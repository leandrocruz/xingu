package br.com.ibnetwork.xingu.utils.clone;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import br.com.ibnetwork.xingu.utils.clone.impl.SimpleCloner;

public class ClonerTest
{
	private void compareArrays(Object[] a1, Object[] a2)
	{
		assertEquals(a1.length, a2.length);
		assertNotSame(a1, a2);
		assertArrayEquals(a1, a2);

		int size = a1.length;
		for (int i = 0; i < size; i++)
		{
			Object item1 = a1[i];
			Object item2 = a2[i];
			assertNotSame(item1, item2);
			assertEquals(item1, item2);
		}
	}

	@Test
	public void testCloneObject()
		throws Exception
	{
		Object o1 = new Object();
		Object o2 = new SimpleCloner().deepClone(o1);
		assertNotSame(o1, o2);
		//assertEquals(o1, o2); fails for objects
	}

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
		SimpleObject o1 = new SimpleObject(10, "Isaiah 61", "Luke 4");
		SimpleObject o2 = cloner.deepClone(o1);
		assertNotSame(o1, o2);
		assertEquals(o1, o2);
	}

	@Test
	public void testCloneNestedObject()
		throws Exception
	{
		Cloner cloner = new SimpleCloner();
		SimpleObject f1 = new SimpleObject(10, "John 3:16", "Indeed");
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
		l1.add(new SimpleObject(1, "One", "Eins"));
		l1.add(new SimpleObject(2, "Two", "Zwei"));
		
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
	public void testCloneObjectWithMap()
		throws Exception
	{
		Map<String, SimpleObject> map1 = new HashMap<String, SimpleObject>();
		map1.put("key 1", new SimpleObject(1, "s1", "w1"));
		map1.put("key 2", new SimpleObject(2, "s2", "w2"));
		
		WithMap o1 = new WithMap(map1);
		WithMap o2 = new SimpleCloner().deepClone(o1);
		assertNotSame(o1, o2);
		
		Map<String, SimpleObject> map2 = o2.map();
		assertNotSame(map1, map2);
		assertEquals(map1, map2);
		
		Set<String> k1 = map1.keySet();
		Set<String> k2 = map2.keySet();
		assertNotSame(k1, k2);
		assertEquals(k1, k2);

		Collection<SimpleObject> v1 = map1.values();
		Collection<SimpleObject> v2 = map2.values();
		assertNotSame(v1, v2);
		//assertEquals(v1, v2); Fails!
		
		compareArrays(v1.toArray(), v2.toArray());
	}

	@Test
	public void testCloneObjectWithArray()
		throws Exception
	{
		Cloner cloner = new SimpleCloner();

		SimpleObject[] a1 = new SimpleObject[] {
				new SimpleObject(1, "One", "Eins"),
				new SimpleObject(2, "Two", "Zwei")
		};
		
		WithArray o1 = new WithArray(a1);
		WithArray o2 = cloner.deepClone(o1);
		assertNotSame(o1, o2);
		
		compareArrays(a1, o2.array());
	}

	@Test
	public void testCloneObjectWithHandler()
		throws Exception
	{
		Cloner cloner = new SimpleCloner();
		cloner.addHandler(IFaceImpl1.class, new ReferenceHandler(){

			@Override
			public Object handle(Object reference)
			{
				return new IFaceImpl2();
			}
		});
		
		WithInterface o1 = new WithInterface(new IFaceImpl1());
		WithInterface o2 = cloner.deepClone(o1);
		assertNotSame(o1, o2);
		
		IFace if1 = o1.iFace();
		IFace if2 = o2.iFace();
		assertNotSame(if1, if2);
		
		assertEquals(10, o1.iFace().value());
		assertEquals(20, o2.iFace().value());
		assertTrue(if2 instanceof IFaceImpl2);
	}

	@Test
	public void testCloneArray()
		throws Exception
	{
		String[] array = new String[]{"a", "b", "c"};
		String[] copy = new SimpleCloner().deepClone(array);
		compareArrays(array, copy);
	}

	@Test
	public void testCloneTreeSet()
		throws Exception
	{
		Set<SimpleObject> s1 = new TreeSet<SimpleObject>(new SimpleObjectComparator());
		s1.add(new SimpleObject(1, "one", "eins"));
		s1.add(new SimpleObject(2, "two", "zwein"));
		
		WithSet<SimpleObject> o1 = new WithSet<SimpleObject>(s1);
		WithSet<SimpleObject> o2 = new SimpleCloner().deepClone(o1);
		
		assertNotSame(o1, o2);
		
		Set<SimpleObject> s2 = o2.set();
		assertNotSame(s1, s2);
		
		compareArrays(s1.toArray(), s2.toArray());
	}

	@Test
	public void testCloneHashSet()
		throws Exception
	{
		Set<SimpleObject> s1 = new HashSet<SimpleObject>();
		s1.add(new SimpleObject(1, "one", "eins"));
		s1.add(new SimpleObject(2, "two", "zwein"));
		
		WithSet<SimpleObject> o1 = new WithSet<SimpleObject>(s1);
		WithSet<SimpleObject> o2 = new SimpleCloner().deepClone(o1);
		
		assertNotSame(o1, o2);
		
		Set<SimpleObject> s2 = o2.set();
		assertNotSame(s1, s2);
		
		//compareArrays(s1.toArray(), s2.toArray()); // The order may change, which breaks the test
	}

	@Test
	public void testNullIsEnforced()
		throws Exception
	{
		Salvation o1 = new Salvation(null);
		Salvation o2 = new SimpleCloner().deepClone(o1);
		assertEquals(null, o2.is());
	}

	@Test
	public void testReferencesAreNotDuplicatedOnStringArray()
		throws Exception
	{
		String item = "a";
		String[] array = new String[]{item, item};
		String[] copy = new SimpleCloner().deepClone(array);

		assertSame(array[0], array[1]);
		assertSame(copy[0], copy[1]);

		compareArrays(array, copy);
	}

	@Test
	public void testReferencesAreNotDuplicatedOnArray()
		throws Exception
	{
		SimpleObject simple = new SimpleObject(1, "Romans 3:10-12", "You are not just at all");
		SimpleObject[] array = new SimpleObject[]{
				simple,
				simple
		};
		
		SimpleObject[] copy = new SimpleCloner().deepClone(array);

		assertSame(array[0], array[1]);
		assertSame(copy[0], copy[1]);
		
		compareArrays(array, copy);
	}

	@Test
	public void testReferencesOfReferencesAreNotDuplicatedOnArray()
		throws Exception
	{
		String s = "Genesis 3";
		SimpleObject[] array = new SimpleObject[]{
				new SimpleObject(1, s, new String("w1")),
				new SimpleObject(2, s, new String("w1"))
		};
		
		SimpleObject[] copy = new SimpleCloner().deepClone(array);
		compareArrays(array, copy);
		
		assertSame(array[0].s(), array[1].s());
		assertNotSame(array[0].w(), array[1].w());
		assertEquals(array[0].w(), array[1].w());
		assertEquals(array[0].i() + 1, array[1].i());

		assertSame(copy[0].s(), copy[1].s());
		assertNotSame(copy[0].w(), copy[1].w());
		assertEquals(copy[0].w(), copy[1].w());
		assertEquals(copy[0].i() + 1, copy[1].i());
	}
	
	@Test
	public void testDate()
		throws Exception
	{
		Date o1 = new Date(1000);
		Date o2 = new SimpleCloner().deepClone(o1);
		assertNotSame(o1, o2);
		assertEquals(o1, o2);
	}
}
