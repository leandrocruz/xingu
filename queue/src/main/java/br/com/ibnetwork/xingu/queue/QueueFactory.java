package br.com.ibnetwork.xingu.queue;

public interface QueueFactory
{
     MessageQueue newQueue(String queueId)
        throws QueueManagerException;
}
