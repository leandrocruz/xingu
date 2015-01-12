package xingu.store;

import xingu.container.Inject;
import xingu.factory.Factory;

public class OtherPojo
    extends Pojo
	implements PersistentBean
{
    private static final long serialVersionUID = 1L;
    
    @Inject
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
