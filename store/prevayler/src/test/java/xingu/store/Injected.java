package xingu.store;

import br.com.ibnetwork.xingu.container.Inject;

public class Injected
    implements PersistentBean
{
    @Inject
    private ObjectStore store;
    
    private long id;
    
    public ObjectStore store()
    {
        return store;
    }

    @Override
    public long getId()
    {
        return id;
    }

    @Override
    public void setId(long id)
    {
        this.id = id;
    }

}
