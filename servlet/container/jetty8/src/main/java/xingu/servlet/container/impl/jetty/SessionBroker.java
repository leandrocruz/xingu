package xingu.servlet.container.impl.jetty;

import org.eclipse.jetty.server.session.SessionHandler;

public interface SessionBroker
{
    SessionHandler handlerFor(String id);
}
