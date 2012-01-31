package br.com.ibnetwork.xingu.idgenerator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import xingu.store.ObjectStore;
import xingu.store.impl.memory.InMemoryObjectStore;
import br.com.ibnetwork.xingu.container.Binder;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;

public class IdGeneratorTest 
	extends XinguTestCase
{
	@Inject
    private IdGenerator idGenerator;

	@Override
    protected void rebind(Binder binder)
    {
	    binder.bind(ObjectStore.class).to(InMemoryObjectStore.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testInMemoryLongGenerator()
		throws Exception
	{
	    Generator<Long> generator = (Generator<Long>) idGenerator.generator("memLong");
	    testGenerator(generator);
	}

    @SuppressWarnings("unchecked")
    @Test
    public void testPersistantLongGenerator()
        throws Exception
    {
        Generator<Long> generator = (Generator<Long>) idGenerator.generator("storedLong");
        testGenerator(generator);
    }
    
    private void testGenerator(Generator<Long> generator)
        throws Exception
    {
        int LOOP = 12;
        long value = generator.state();
        long last = value;
        for(int i=1 ; i<=LOOP ; i++)
        {
            value = generator.next();
            assertEquals(last+1, value);
            last = value;
        }
        assertEquals(new Long(last), generator.state());
    }
}
