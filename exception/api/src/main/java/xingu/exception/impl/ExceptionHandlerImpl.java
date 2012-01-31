package xingu.exception.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.exception.ExceptionHandler;

public class ExceptionHandlerImpl
    implements ExceptionHandler
{
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public void handle(Throwable throwable)
    {
        handle(throwable, null);
    }

    @Override
    public void handle(Throwable error, Thread thread)
    {
        if(error instanceof ExceptionWithData)
        {
            ExceptionWithData withData = (ExceptionWithData) error;
            Object data = withData.data();
            if(data != null)
            {
                logger.info("Error '"+data+"' handling exception", error);
            }
        }
        else
        {
            logger.info("Error handling exception", error);
        }
    }
}
