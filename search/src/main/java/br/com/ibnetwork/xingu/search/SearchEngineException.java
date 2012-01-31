package br.com.ibnetwork.xingu.search;

import org.apache.commons.lang.exception.NestableRuntimeException;

@SuppressWarnings("serial")
public class SearchEngineException
    extends NestableRuntimeException
{
    public SearchEngineException(String message, Throwable t)
    {
        super(message,t);
    }
}
