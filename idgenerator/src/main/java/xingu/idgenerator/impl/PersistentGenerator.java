package xingu.idgenerator.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.container.Inject;
import xingu.idgenerator.GeneratorException;
import xingu.store.ObjectStore;

public abstract class PersistentGenerator<T>
    extends GeneratorSupport<T>
{
    @Inject
    protected ObjectStore store;

    private GeneratorState<T> initialState;
    
    private Logger log = LoggerFactory.getLogger(getClass());
    
    public PersistentGenerator(String id, int grabSize)
    {
        super(id, grabSize);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected T loadInitialState() 
        throws GeneratorException
    {
        List<GeneratorState> states = store.getAll(GeneratorState.class);
        T result = null;

        for (GeneratorState generatorState : states)
        {
            if(generatorState.generatorId().equals(generatorId))
            {
                initialState = generatorState; 
                result = initialState.state();
            }
        }
        log.info("Loading state for generator '{}' state: {}", generatorId, result);
        
        if(initialState == null)
        {
            result = firstState();
            initialState = new GeneratorState(generatorId);
        }
        else
        {
            for(int i=0; i<grabSize; i++)
            {
                result = increment(result);
            }
        }
        log.info("Starting generator '{}' at: {}", generatorId, result);
        updateState(result);
        return result;
    }

    protected abstract T firstState();

    @Override
    protected void updateState(T state) 
        throws GeneratorException
    {
        log.info("Storing generator '{}' state: {}", generatorId, state);
        initialState.state(state);
        store.store(initialState);
    }
}
