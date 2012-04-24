package xingu.servlet.container.impl.jetty;

import org.mortbay.jetty.Handler;

public interface HandlerFactory
{
    Handler createHandler()
        throws Exception;
}
