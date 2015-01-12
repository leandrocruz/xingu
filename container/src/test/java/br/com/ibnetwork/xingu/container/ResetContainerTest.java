package br.com.ibnetwork.xingu.container;

import static org.junit.Assert.assertNotSame;

import org.junit.Test;

import xingu.container.Inject;
import xingu.container.XinguTestCase;
import br.com.ibnetwork.xingu.container.components.Component;

public class ResetContainerTest
    extends XinguTestCase
{
    private static Component instance;
    
    @Inject
    private Component c;
    
    @Override
    protected boolean resetContainer()
    {
        return true;
    }

    @Test
    public void getComponent()
    {
        instance = c;
    }

    @Test
    public void getComponentAgain()
    {
        assertNotSame(instance, c);
    }
}
