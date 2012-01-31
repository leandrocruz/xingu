package br.com.ibnetwork.xingu.idgenerator.impl;

import br.com.ibnetwork.xingu.idgenerator.GeneratorException;

public class LongGenerator
	extends PersistentGenerator<Long>
{
    public LongGenerator(String generatorId, int grabSize)
    {
        super(generatorId, grabSize);
    }

    @Override
    protected Long firstState()
    {
        return 0l;
    }

    @Override
    protected Long increment(Long lastUsed) 
        throws GeneratorException
    {
        return lastUsed + 1;
    }
}
