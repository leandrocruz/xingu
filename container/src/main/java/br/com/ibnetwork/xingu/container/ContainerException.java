package br.com.ibnetwork.xingu.container;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class ContainerException
        extends NestableRuntimeException
{
    private static final long serialVersionUID = 1L;

    public ContainerException(String message)
    {
        super(message);
    }

    public ContainerException(String message, Throwable t)
    {
        super(message,t);
    }

}
