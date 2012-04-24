package xingu.servlet.container.impl.jetty.impl;

import org.mortbay.jetty.servlet.SessionHandler;
import xingu.servlet.container.impl.jetty.SessionBroker;

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
