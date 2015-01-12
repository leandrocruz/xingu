package xingu.idgenerator.impl;

import xingu.idgenerator.GeneratorException;

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
