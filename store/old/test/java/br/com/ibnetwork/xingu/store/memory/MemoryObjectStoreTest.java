package br.com.ibnetwork.xingu.store.memory;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import br.com.ibnetwork.xingu.container.Dependency;
import br.com.ibnetwork.xingu.container.XinguTestCase;
import br.com.ibnetwork.xingu.store.ObjectStore;
import br.com.ibnetwork.xingu.store.Pojo;

public class MemoryObjectStoreTest
    extends XinguTestCase
{
    @Dependency("memory")
    private ObjectStore store;
    
    @Override
    protected boolean resetContainer()
    {
        return true;
    }
    
    @Test
    public void testStore()
    {
        Pojo pojo = new Pojo();
        pojo.setName("Elephant");
        assertEquals(0, pojo.getId());
        
        store.store(pojo);
        assertEquals(1, pojo.getId());
        
        pojo = new Pojo();
        pojo.setName("Dog");
        assertEquals(0, pojo.getId());
        
        store.store(pojo);
        assertEquals(2, pojo.getId());
    }
    
    
    @Test
    public void testStoreTwice()
    {
        Pojo pojo = new Pojo();
        pojo.setName("Eagle");
        
        store.store(pojo);
        assertEquals(1, pojo.getId());
        
        List<Pojo> list = store.getAll(Pojo.class);
        assertEquals(1, list.size());
        
        store.store(pojo);
        assertEquals(1, pojo.getId());
        
        list = store.getAll(Pojo.class);
        assertEquals(1, list.size());
    }
    
    @Test
    public void testGetAll()
    {
        Pojo pojo = new Pojo();
        pojo.setName("Horse");
        store.store(pojo);
        
        pojo = new Pojo();
        pojo.setName("Bird");
        store.store(pojo);
        
        pojo = new Pojo();
        pojo.setName("Alligator");
        store.store(pojo);
        
        List<Pojo> list = store.getAll(Pojo.class);
        assertEquals(3, list.size());
        
        pojo = list.get(0);
        assertEquals("Horse", pojo.getName());
        
        pojo = list.get(1);
        assertEquals("Bird", pojo.getName());
        
        pojo = list.get(2);
        assertEquals("Alligator", pojo.getName());
    }
}
