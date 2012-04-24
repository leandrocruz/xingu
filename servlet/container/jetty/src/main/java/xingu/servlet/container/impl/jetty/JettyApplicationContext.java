package xingu.servlet.container.impl.jetty;

import java.io.IOException;

import xingu.servlet.container.ApplicationContext;

import org.mortbay.jetty.webapp.WebAppContext;

public class JettyApplicationContext
    implements ApplicationContext
{
    private WebAppContext context;

    public JettyApplicationContext(WebAppContext context)
    {
        this.context = context;
    }

    @Override
    public String name()
    {
        return context.getContextPath();
    }

    @Override
    public void start() 
        throws Exception
    {
        if(!context.isRunning())
        {
            context.start();
        }
    }

    @Override
    public void stop() 
        throws Exception
    {
        if(context.isRunning())
        {
            context.stop();
        }
    }

    @Override
    public boolean isRunning()
    {
        return context.isRunning();
    }

    @Override
    public String war()
    {
        return context.getWar();
    }

    @Override
    public String url()
    {
        try
        {
            return context.getBaseResource().getFile().getAbsolutePath();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}