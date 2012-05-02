package xingu.servlet.container.impl.jetty;

import java.util.ArrayList;
import java.util.List;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.handler.HandlerCollection;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;

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
            String className = handlerConf.getAttribute("class");
            HandlerFactory handler = (HandlerFactory) factory.create(className, handlerConf);
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