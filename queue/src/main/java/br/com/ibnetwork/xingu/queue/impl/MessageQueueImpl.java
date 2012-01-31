package br.com.ibnetwork.xingu.queue.impl;

import br.com.ibnetwork.xingu.queue.Message;


class MessageQueueImpl
    extends MessageQueueSupport
{
    private long counter = 0;
    
    public MessageQueueImpl(String id)
    {
        super(id);
    }

    @Override
    protected long generateIdForMessage(Message msg)
    {
        if(counter == 0)
        {
            counter = list.size() + 1;
        }
        return counter++;
    }
}
