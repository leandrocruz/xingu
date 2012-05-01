package xingu.servlet.container.impl.jetty;

import org.apache.avalon.framework.configuration.Configurable;
import org.eclipse.jetty.server.Server;

public interface ServerConfig
    extends Configurable
{
    void applyTo(Server server)
        throws Exception;
}
