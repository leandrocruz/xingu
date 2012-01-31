package br.com.ibnetwork.xingu.container.components.impl;

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.logger.Logger;

import br.com.ibnetwork.xingu.container.components.LogEnabledComponent;

public class LogEnabledComponentImpl
    extends AbstractLogEnabled
    implements LogEnabledComponent
{
    private Logger logger;
    
    public void enableLogging(Logger logger)
    {
        this.logger = logger;
    }

    public Logger getLoggerObject()
    {
        return logger;
    }
}
