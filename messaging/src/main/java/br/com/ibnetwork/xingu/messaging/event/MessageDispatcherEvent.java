package br.com.ibnetwork.xingu.messaging.event;

import javax.mail.Message;

//import br.com.ibnetwork.xingu.sandbox.event.Event;
//import br.com.ibnetwork.xingu.sandbox.event.impl.EventSupport;

public class MessageDispatcherEvent
/*    extends EventSupport
    implements Event */
{
    protected javax.mail.Message message;
    
    public MessageDispatcherEvent(Object source, Message message)
    {
        /* super(source); */
        this.message = message;
    }

    public javax.mail.Message getMessage()
    {
        return message;
    }

}
