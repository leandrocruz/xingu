package xingu.store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Random;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

import xingu.store.impl.prevayler.PrevaylerObjectStore;
import br.com.ibnetwork.xingu.container.Binder;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;
import br.com.ibnetwork.xingu.factory.Factory;
import br.com.ibnetwork.xingu.utils.io.FileUtils;

public class PrevaylerTest
    extends XinguTestCase
{
    @Inject
	private ObjectStore store;
	
	@Inject
	private Factory factory;

    @Override
    protected String getContainerFile()
    {
        return "pulga-empty.xml";
    }

    @Override
    protected void rebind(Binder binder)
    	throws Exception
    {
    	File prevaylerDir = FileUtils.createTempDir("prevayler-");
    	String xml = "<component> <prevayler	prevalenceDirectory=\""+prevaylerDir+"\" clearPrevalenceDirectory=\"true\" takeSnapshotOnStart=\"false\"/> </component>";
    	Configuration conf = buildFrom(xml);
    	binder.bind(ObjectStore.class).to(PrevaylerObjectStore.class).with(conf);
    }

	
    @Test
	public void testAddWithId()
		throws Exception
	{
		long id = uniqueId();
		Pojo pojo = new Pojo(id, "java");
		store.store(pojo);
		
		Pojo stored = store.getById(Pojo.class,id);
		assertNotSame(pojo, stored);
		assertEquals(id,stored.getId());
		assertEquals("java",stored.getName());
	}

    private long uniqueId()
    {
        Pojo pojo = null;
        long id;
        do
        {
            id = RandomUtils.nextLong(new Random(System.currentTimeMillis()));
            if(id <=0)
            {
                pojo = store.getById(Pojo.class,id);
            }
        }
        while(pojo != null || id <= 0);
        return id;
    }

	@Test
	public void testAdd()
		throws Exception
	{
		Pojo pojo = new Pojo(0, "java ZERO");
		store.store(pojo);

		assertTrue(pojo.getId() > 0);
		long id = pojo.getId();
		Pojo stored = store.getById(Pojo.class,id);
		
		assertEquals(id,stored.getId());
		assertEquals("java ZERO",stored.getName());
		assertNotSame(pojo, stored);
	}

	@Test
	public void testUpdate()
		throws Exception
	{
	    Pojo tmp = new Pojo(-1, "java");
	    store.store(tmp);
	    long id = tmp.getId();
		Pojo pojo = store.getById(Pojo.class,id);

		assertNotSame(tmp, pojo);
		assertEquals(id,pojo.getId());
		assertEquals("java",pojo.getName());
		
		List<Pojo> list = store.getAll(Pojo.class);
		int size = list.size();
		
		pojo.setName("new");
		store.store(pojo);
		
		pojo = store.getById(Pojo.class,id);
		assertEquals(id,pojo.getId());
		assertEquals("new",pojo.getName());
		
		list = store.getAll(Pojo.class);
		assertEquals(size, list.size());
	}

	@Test
	public void testRemove()
		throws Exception
	{
	    Pojo pojo = new Pojo();
	    store.store(pojo);
	    long id = pojo.getId();
	    
		Pojo stored = store.getById(Pojo.class, id);
		assertEquals(id, stored.getId());
		
		store.delete(stored);
		Pojo Pojo = store.getById(Pojo.class,id);
		assertNull(Pojo);
	}

	@Test
	public void testRemoveNotStored()
		throws Exception
	{
	    long id = uniqueId();
		Pojo pojo = store.getById(Pojo.class,id);
		assertNull(pojo);

		pojo = new Pojo(1000, "java");
		store.delete(pojo);
	}
	
	@Test
	public void testDependencyInjection()
	{
	    OtherPojo pojo = (OtherPojo) factory.create(OtherPojo.class);
	    store.store(pojo);
	    
	    OtherPojo other = store.getById(OtherPojo.class,1);
	    assertNotSame(pojo, other); //prevayler stores a copy of your object
	    assertTrue(other.getFactory() != null);
	}

	@Test
	public void testPolymorficQuery()
	    throws Exception
	{
	    PersistentBean bean = factory.create(MyObject1.class);
	    store.store(bean);

	    bean = factory.create(MyObject1.class);
	    store.store(bean);
	    
	    long id = bean.getId();
	    PersistentBean stored = store.getById(MyInterface.class, id);
	    assertEquals(id, stored.getId());
	    assertTrue(stored instanceof MyObject1);
	    assertTrue(stored instanceof MyInterface);

	    bean = factory.create(MyObject2.class);
	    store.store(bean);

	    List<MyObject1> list1 = store.getAll(MyObject1.class);
	    assertEquals(2, list1.size());

	    List<MyObject2> list2 = store.getAll(MyObject2.class);
	    assertEquals(1, list2.size());

	    List<MyInterface> list = store.getAll(MyInterface.class);
	    assertEquals(3, list.size());

	}
	
//	@Test
//	public void testPrevaylerDao()
//	    throws Exception
//	{
//	    PrevalentObjectDao dao = daoManager.getDao(PrevalentObject.class);
//	    assertTrue(dao instanceof PrevalentObjectDao);
//	    
//	    dao.setStore(store); //prevent StackOverflowError
//	    
//	    PrevalentObject obj = dao.getById(1l);
//	    assertNull(obj);
//	    
//	    obj = new PrevalentObjectImpl();
//	    dao.insert(obj);
//	    
//	    obj = dao.getById(1l);
//        assertEquals(1, obj.getId());
//	}
	
	@Test
	public void testInjection()
	    throws Exception
	{
	    Injected injected = new Injected();
	    store.store(injected);
	    Injected stored = store.getById(Injected.class, injected.getId());
	    assertNotNull(stored.store());
	}
	
	@Test
	public void testIsAssignableFrom()
	{
	    assertTrue(PersistentBean.class.isAssignableFrom(PersistentBean.class));
	    assertTrue(PersistentBean.class.isAssignableFrom(MyInterface.class));
	    assertTrue(MyInterface.class.isAssignableFrom(MyObject1.class));
	    assertTrue(MyInterface.class.isAssignableFrom(MyObject2.class));
	    assertFalse(MyObject1.class.isAssignableFrom(MyInterface.class));
	    assertFalse(MyObject2.class.isAssignableFrom(MyInterface.class));
	}
	
	@Test
	public void testIdGenerator()
	    throws Exception
	{
	    Pojo p1 = new Pojo();
	    store.store(p1);
	    
	    Pojo p2 = new Pojo();
	    store.store(p2);
	    
	    assertTrue(p2.getId() > p1.getId());
	}
	
	@Test
	public void testConcurrentAccess()
	    throws Exception
	{
	    Thread t1 = new Thread(new X(store));
	    t1.setName("T1");
	    Thread t2 = new Thread(new X(store));
	    t2.setName("T2");
        Thread t3 = new Thread(new X(store));
        t3.setName("T3");

	    t1.start();
	    t2.start();
	    t3.start();
	    
	    t1.join();
	    t2.join();
	    t3.join();
	    
	    assertEquals(300, X.id);
	}
}

class X implements Runnable
{
    private ObjectStore store;
    
    static long id;
    
    public X(ObjectStore store)
    {
        this.store = store;
    }

    @Override
    public void run()
    {
        for(int i=0 ; i<100 ; i++)
        {
            PersistentBean bean = new MyBean();
            store.store(bean);
            long latest = bean.getId(); 
            //System.out.println("["+Thread.currentThread().getName()+"] "+latest);
            if(latest > id)
            {
                id = latest;
            }
        }
    }
}

class MyBean
    implements PersistentBean
{

    private long id;
    
    @Override
    public long getId()
    {
        return id;
    }

    @Override
    public void setId(long id)
    {
        this.id = id;
    }
}