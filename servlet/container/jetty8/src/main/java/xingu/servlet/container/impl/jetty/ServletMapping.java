package xingu.servlet.container.impl.jetty;

import javax.servlet.Servlet;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import xingu.container.Inject;
import xingu.factory.Factory;
import xingu.utils.ObjectUtils;

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
    	Class<?>      clazz   = ObjectUtils.loadClass(className);
        Servlet       servlet = (Servlet) factory.create(clazz, servletConf);
        ServletHolder holder  = new ServletHolder(servlet);
        servletHandler.addServletWithMapping(holder, path);
    }
    
    @Override
    public String toString()
    {
        return "path '"+path+"' className '"+className+"'";
    }
}
