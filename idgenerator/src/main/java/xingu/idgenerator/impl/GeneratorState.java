package xingu.idgenerator.impl;

import xingu.store.PersistentBean;

public class GeneratorState<T>
    implements PersistentBean
{
    private long id;
    
    private T state;
    
    private String generatorId;
    
    public GeneratorState()
    {}

    public GeneratorState(String generatorId)
    {
        this.generatorId = generatorId;
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

    public String generatorId()
    {
        return generatorId;
    }

    public T state()
    {
        return state;
    }

    public void state(T state)
    {
        this.state = state;
    }

    @Override
    public String toString()
    {
        return "state: '"+state+"'";
    }
}
