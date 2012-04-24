package xingu.servlet.container.impl.jetty;

import org.mortbay.jetty.servlet.SessionHandler;

public class SessionBrokerImpl
    implements SessionBroker
{
    private SessionHandler sessionHandler;
    
    @Override
    public SessionHandler handlerFor(String id /*ignored*/)
    {
        if(sessionHandler == null)
        {
            sessionHandler = createSessionHandler();
        }
        return sessionHandler;
    }

    private SessionHandler createSessionHandler()
    {
        return new SessionHandler();
    }
}
