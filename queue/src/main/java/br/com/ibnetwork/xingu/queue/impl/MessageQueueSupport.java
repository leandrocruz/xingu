package br.com.ibnetwork.xingu.queue.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.queue.Message;
import br.com.ibnetwork.xingu.queue.MessageQueue;
import br.com.ibnetwork.xingu.queue.QueueFactory;

public abstract class MessageQueueSupport
    implements MessageQueue
{
    protected String id;
    
    protected List<Message> list = new ArrayList<Message>();
    
    @Inject
    protected QueueFactory factory;

    protected abstract long generateIdForMessage(Message msg);

    public MessageQueueSupport(String id)
    {
        this.id = id;
    }

    public String id()
    {
        return id;
    }

    public int count()
    {
        return list.size();
    }

    public Message peak()
    {
        int count = count();
        if(count == 0)
        {
            return null;
        }
        return list.get(count - 1);
    }

    public void enqueue(Message msg)
    {
        long messageId = generateIdForMessage(msg);
        msg.setId(messageId);
        list.add(msg);
    }

    public Message delete(long messageId)
    {
        Iterator<Message> it = list.iterator();
        while (it.hasNext())
        {
            Message message = it.next();
            if(message.id() == messageId)
            {
                it.remove();
                return message;
            }
        }
        return null;
    }
}
