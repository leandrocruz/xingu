package br.com.ibnetwork.xingu.idgenerator.impl;

import br.com.ibnetwork.xingu.idgenerator.GeneratorException;

public class InMemoryLongGenerator
	extends GeneratorSupport<Long>
{
    public InMemoryLongGenerator(String generatorId, int grabSize)
    {
        super(generatorId, grabSize);
    }

    @Override
    protected Long increment(Long lastUsed) 
        throws GeneratorException
    {
        return lastUsed + 1;
    }

    @Override
    protected Long loadInitialState() 
        throws GeneratorException
    {
        return 0l;
    }

    @Override
    protected void updateState(Long state) 
        throws GeneratorException
    {}
}
