package br.com.ibnetwork.xingu.idgenerator.impl;

import org.apache.avalon.framework.activity.Initializable;

import br.com.ibnetwork.xingu.idgenerator.Generator;
import br.com.ibnetwork.xingu.idgenerator.GeneratorException;

public abstract class GeneratorSupport<T>
	implements Generator<T>, Initializable
{
	protected T state;
	
	protected String generatorId;

	protected int grabSize;
	
	protected int callsSinceLastGrab;

	public GeneratorSupport(String generatorId, int grabSize)
	{
	    this.generatorId = generatorId;
	    this.grabSize = grabSize;
	}
	
	@Override
	public void initialize() 
		throws Exception
	{
	    state = loadInitialState();
	}
	
	@Override
	public T state()
	{
	    return state;
	}
	
	@Override
	public synchronized T next()
		throws GeneratorException
	{
	    state = increment(state);
		callsSinceLastGrab++;

	    if(grabSize > 0 && callsSinceLastGrab >= grabSize)
		{
            updateState(state);
			callsSinceLastGrab = 0;
		}
		return state;
	}

    protected abstract T increment(T lastUsed)
        throws GeneratorException;

    protected abstract T loadInitialState()
        throws GeneratorException;

	protected abstract void updateState(T state)
        throws GeneratorException;
}