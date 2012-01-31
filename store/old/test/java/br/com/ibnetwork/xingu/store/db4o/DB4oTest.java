package br.com.ibnetwork.xingu.store.db4o;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Ignore;
import org.junit.Test;

import br.com.ibnetwork.xingu.container.Dependency;
import br.com.ibnetwork.xingu.container.XinguTestCase;
import br.com.ibnetwork.xingu.store.ObjectStore;
import br.com.ibnetwork.xingu.store.OtherPojo;
import br.com.ibnetwork.xingu.store.Pojo;

public class DB4oTest
    extends XinguTestCase
{
    @Dependency("db4o")
    private ObjectStore store;
    
    @Test
    @Ignore
    public void testStoreObject()
        throws Exception
    {
        Pojo pojo = store.getById(Pojo.class, 1);
        OtherPojo other;
        
        assertNull(pojo);
        
        pojo = new Pojo(1, "name");
        other = new OtherPojo(1,"other");
        
        store.store(pojo);
        store.store(other);
        
        Pojo stored = store.getById(Pojo.class, 1);
        assertEquals(1, stored.getId());
        assertSame(pojo, stored);

        Pojo otherStored = store.getById(OtherPojo.class, 1);
        assertEquals(1, otherStored.getId());
        assertSame(other, otherStored);

        store.store(new OtherPojo(2, "other2"));

        otherStored = store.getById(OtherPojo.class, 2);
        assertEquals(2, otherStored.getId());
    }
}
