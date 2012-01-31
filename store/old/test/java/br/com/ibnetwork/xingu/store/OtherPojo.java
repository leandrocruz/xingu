package br.com.ibnetwork.xingu.store;

import br.com.ibnetwork.xingu.container.Dependency;
import br.com.ibnetwork.xingu.factory.Factory;

public class OtherPojo
    extends Pojo
	implements PersistentBean
{
    private static final long serialVersionUID = 1L;
    
    @Dependency
    private transient Factory factory;

    public OtherPojo()
    {}
    
    public OtherPojo(long id, String name)
    {
        super(id, name);
    }

    public Factory getFactory()
    {
        return factory;
    }

    public void setFactory(Factory factory)
    {
        this.factory = factory;
    }
}
