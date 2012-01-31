package xingu.store;

public class PrevalentObjectImpl
    implements PrevalentObject
{
    private long id;
    
    public String getDisplay()
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

}
