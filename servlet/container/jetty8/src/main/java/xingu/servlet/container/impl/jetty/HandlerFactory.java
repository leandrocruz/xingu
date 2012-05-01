package xingu.servlet.container.impl.jetty;

import org.eclipse.jetty.server.Handler;

public interface HandlerFactory
{
    Handler createHandler()
        throws Exception;
}
