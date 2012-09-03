package br.com.ibnetwork.xingu.container;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Ignore;
import org.junit.Test;

import br.com.ibnetwork.xingu.container.components.AnotherComponent;
import br.com.ibnetwork.xingu.container.components.Component;

/**
 * The test case should not rebind dependencies more
 * than once when resetContainer() is set to false.
 */
public class RebindOnceTest
    extends XinguTestCase
{
    private static Component instance;
    
    @Inject
    private Component c;
    
    @Override
    protected boolean resetContainer()
    {
        return false;
    }
    
    @Override
    protected void rebind(Binder binder)
    {
        binder.bind(Component.class).to(AnotherComponent.class);
    }

    /**
     * Have to number test methods to guarantee order of execution.
     */
    @Test
    public void testGetComponent()
    {
    	//System.out.println(Thread.currentThread() + " 1 " + container);
        assertNotNull(c);
        assertEquals(AnotherComponent.class, c.getClass());
        instance = c;
    }

    @Test
    @Ignore("Implies a test order wich breaks on maven somtimes")
    public void testGetComponentAgain()
    {
    	//System.out.println(Thread.currentThread() + " 2 " + container);
        assertSame(instance, c);
    }
}
