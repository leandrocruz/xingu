package br.com.ibnetwork.xingu.container.components.impl;

import br.com.ibnetwork.xingu.container.Container;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.components.Component;
import br.com.ibnetwork.xingu.container.components.Simple;
import br.com.ibnetwork.xingu.container.components.UsesAnnotations;

public class UsesAnnotationsImpl
    extends UsesAnnotationsSupport
    implements UsesAnnotations
{
    @Inject
    protected Container manager;
    
    @Inject
    protected Simple Inject1;

    @Inject("alternative")
    private Simple Inject2;
    
    public Simple getD1()
    {
        return Inject1;
    }

    public Simple getD2()
    {
        return Inject2;
    }

    public Component getD3()
    {
        return Inject3;
    }

    public Container getContainer()
    {
        return manager;
    }
}
