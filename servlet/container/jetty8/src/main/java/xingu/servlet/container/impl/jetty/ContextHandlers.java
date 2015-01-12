package xingu.servlet.container.impl.jetty;

import java.util.ArrayList;
import java.util.List;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;

import xingu.container.Inject;
import xingu.factory.Factory;
import xingu.utils.ObjectUtils;

public class ContextHandlers
    implements ServerConfig
{
    @Inject
    private Factory factory;
    
    private List<HandlerFactory> handlers = new ArrayList<HandlerFactory>();
    
    @Override
    public void configure(Configuration conf)
        throws ConfigurationException
    {
        for(Configuration handlerConf : conf.getChild("server").getChildren("handler"))
        {
            String         className = handlerConf.getAttribute("class");
            Class<?>       clazz     = ObjectUtils.loadClass(className);
            HandlerFactory handler   = (HandlerFactory) factory.create(clazz, handlerConf);
            handlers.add(handler);
        }
    }
    
    @Override
    public void applyTo(Server server)
        throws Exception
    {
        /*
         * @see http://jetty.codehaus.org/jetty/jetty-6/xref/org/mortbay/jetty/example/LikeJettyXml.html
         * @see http://docs.codehaus.org/display/JETTY/Walkthrough+jetty.xml
         */
        HandlerCollection mainHandler = new HandlerCollection();
        ContextHandlerCollection chain = new ContextHandlerCollection();
        mainHandler.setHandlers(new Handler[]{chain/*, new DefaultHandler() */});
        server.setHandler(mainHandler);

        for(HandlerFactory handlerFactory : handlers)
        {
            Handler handler = handlerFactory.createHandler();
            if(handler != null)
            {
                chain.addHandler(handler);
            }
        }
    }
}
