package br.com.ibnetwork.xingu.store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import br.com.ibnetwork.xingu.container.Dependency;
import br.com.ibnetwork.xingu.container.XinguTestCase;

public class ObjectStoreTest
    extends XinguTestCase
{
	@Dependency
	private ObjectStore store;
	
	private static long ID;
	
//	public static TestSuite suite()
//	{
//		TestSuite suite = new TestSuite();
//		suite.addTest(new ObjectStoreTest("testGetById"));
//		suite.addTest(new ObjectStoreTest("testGetAll"));
//		suite.addTest(new ObjectStoreTest("testInsert"));
//		suite.addTest(new ObjectStoreTest("testUpdate"));
//		suite.addTest(new ObjectStoreTest("testDelete"));
//		return suite;
//	}
	
	@Test
	public void testGetById()
		throws Exception
	{
		Pojo pojo = store.getById(Pojo.class, 1);
		assertEquals(1, pojo.getId());
		assertEquals("pojo 1", pojo.getName());
	}
	
	@Test
	public void testGetAll()
		throws Exception
	{
		List list = store.getAll(Pojo.class);
		assertEquals(2,list.size());
	}
	
	@Test
	public void testInsert()
		throws Exception
	{
		Pojo pojo = new Pojo();
		assertEquals(0,pojo.getId());
		store.insert(pojo);
		ID = pojo.getId();
		assertTrue(ID > 0);
		assertEquals(null,pojo.getName());
		
		pojo = store.getById(Pojo.class, ID);
		assertEquals(ID,pojo.getId());
		assertEquals(null,pojo.getName());
		
	}

	@Test
	@Ignore
	public void testUpdate() 
		throws Exception
	{
		Pojo pojo = store.getById(Pojo.class, ID);
		System.out.println(store);
		assertEquals(ID, pojo.getId());
		assertEquals(null, pojo.getName());
		
		String name = "pojo "+ID; 
		pojo.setName(name);
		store.update(pojo);
		
		pojo = store.getById(Pojo.class, ID);
		assertEquals(ID, pojo.getId());
		assertEquals(name, pojo.getName());
	}

	@Test
	public void testDelete() 
		throws Exception
	{
		Pojo pojo = store.getById(Pojo.class, ID);
		assertEquals(ID, pojo.getId());

		store.delete(pojo);
		
		pojo = store.getById(Pojo.class, ID);
		assertNull(pojo);
	}

}
