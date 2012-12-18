package br.com.ibnetwork.xingu.factory;

public class FactoryException
    extends RuntimeException
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
