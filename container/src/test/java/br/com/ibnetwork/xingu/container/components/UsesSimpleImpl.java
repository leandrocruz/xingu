package br.com.ibnetwork.xingu.container.components;

import org.apache.avalon.framework.configuration.Configurable;

import xingu.container.Inject;

/**
 * @author <a href="mailto:leandro@ibnetwork.com.br">leandro</a>
 */
public class UsesSimpleImpl
	extends TestComponentSupport
	implements UsesSimple, Configurable
{
    @Inject
    private Simple simple;
    
    public Simple getSimple()
    {
        return simple;
    }
}
