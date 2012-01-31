package br.com.ibnetwork.xingu.factory;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class FactoryException
    extends NestableRuntimeException
{

    public FactoryException(String message)
    {
        super(message);
    }

    public FactoryException(String message, Throwable t)
    {
        super(message,t);
    }

}
