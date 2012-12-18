package br.com.ibnetwork.xingu.container;

public class ContainerException
        extends RuntimeException
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
