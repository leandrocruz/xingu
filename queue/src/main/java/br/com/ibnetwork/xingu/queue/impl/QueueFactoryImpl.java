package br.com.ibnetwork.xingu.queue.impl;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.Injector;
import br.com.ibnetwork.xingu.queue.MessageQueue;
import br.com.ibnetwork.xingu.queue.QueueFactory;
import br.com.ibnetwork.xingu.queue.QueueManagerException;

public class QueueFactoryImpl
    implements QueueFactory
{
    @Inject
    private Injector injector;
    
    public MessageQueue newQueue(String queueId)
    {
        MessageQueue result = new MessageQueueImpl(queueId); 
        try
        {
            injector.injectDependencies(result);
        }
        catch (Exception e)
        {
            throw new QueueManagerException("Error creating MessageQueue", e);
        }
        return result;
    }
}
