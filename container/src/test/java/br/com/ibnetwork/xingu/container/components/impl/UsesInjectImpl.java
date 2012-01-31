package br.com.ibnetwork.xingu.container.components.impl;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.components.Simple;
import br.com.ibnetwork.xingu.container.components.UsesInject;

public class UsesInjectImpl
    implements UsesInject
{
    @Inject
    private Simple simple;
    
    @Override
    public Simple getSimple()
    {
        return simple;
    }

}
