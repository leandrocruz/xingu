package xingu.servlet.container.impl.jetty.impl;

import org.eclipse.jetty.server.session.SessionHandler;
import xingu.servlet.container.impl.jetty.SessionBroker;

public class SessionBrokerImpl
    implements SessionBroker
{
    private SessionHandler sessionHandler;
    
    @Override
    public SessionHandler handlerFor(String id /*ignored*/)
    {
        return new SessionHandler();
    }
}
