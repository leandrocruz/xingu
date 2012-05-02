package xingu.servlet.container.impl.jetty;

import javax.servlet.Servlet;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.eclipse.jetty.servlet.ServletHandler;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.factory.Factory;
import org.eclipse.jetty.servlet.ServletHolder;

@Deprecated
class ServletMapping
    implements Configurable
{
    @Inject
    private Factory factory;
    
    private String className;

    private String path;

    private Configuration servletConf;

    @Override
    public void configure(Configuration conf)
        throws ConfigurationException
    {
        className = conf.getAttribute("class");
        path = conf.getAttribute("path");
        this.servletConf = conf;
    }

    public void addServletTo(ServletHandler servletHandler)
    {
        Servlet servlet = (Servlet) factory.create(className, servletConf);
        ServletHolder holder = new ServletHolder(servlet);
        servletHandler.addServletWithMapping(holder, path);
    }
    
    @Override
    public String toString()
    {
        return "path '"+path+"' className '"+className+"'";
    }
}