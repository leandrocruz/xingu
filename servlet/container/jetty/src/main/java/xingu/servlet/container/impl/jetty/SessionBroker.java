package xingu.servlet.container.impl.jetty;

import org.mortbay.jetty.servlet.SessionHandler;

public interface SessionBroker
{

    SessionHandler handlerFor(String id);
}
