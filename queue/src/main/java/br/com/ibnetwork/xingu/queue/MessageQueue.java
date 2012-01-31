package br.com.ibnetwork.xingu.queue;

public interface MessageQueue
{
    String id();
    
    void enqueue(Message msg);
     
    Message peak();
     
    int count();

    Message delete(long messageId);

    //Message createMessage(T payload);
}
