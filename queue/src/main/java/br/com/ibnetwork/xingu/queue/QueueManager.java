package br.com.ibnetwork.xingu.queue;

import java.util.List;

public interface QueueManager
{
     MessageQueue queue(String id);
    
     MessageQueue newQueue(String id)
        throws QueueManagerException;

    List<MessageQueue> list();

    void clear();
}
