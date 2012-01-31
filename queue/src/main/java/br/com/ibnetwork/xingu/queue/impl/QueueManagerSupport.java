package br.com.ibnetwork.xingu.queue.impl;

import java.util.ArrayList;
import java.util.List;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.queue.MessageQueue;
import br.com.ibnetwork.xingu.queue.QueueFactory;
import br.com.ibnetwork.xingu.queue.QueueManager;
import br.com.ibnetwork.xingu.queue.QueueManagerException;
import br.com.ibnetwork.xingu.utils.StringUtils;

public abstract class QueueManagerSupport
    implements QueueManager
{
    @Inject
    protected QueueFactory factory;

    protected List<MessageQueue> list = new ArrayList<MessageQueue>(); 
    
    public List<MessageQueue> list()
    {
        return list;
    }

    public void clear()
    {
        list.clear();
    }

    public  MessageQueue newQueue(String id)
    {
        if(StringUtils.isEmpty(id))
        {
            throw new QueueManagerException("Queue id is null");
        }
        MessageQueue q = queue(id);
        if(q != null)
        {
            throw new QueueManagerException("Queue ["+id+"] already exists");
        }
        q = factory.newQueue(id);
        list.add(q);
        return q;
    }

    public  MessageQueue queue(String id)
    {
        for (MessageQueue q : list)
        {
            if(q.id().equals(id))
            {
                return (MessageQueue) q;
            }
        }
        return null;
    }
}
