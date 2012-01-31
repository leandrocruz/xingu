package br.com.ibnetwork.xingu.queue;

public class QueueManagerException
    extends RuntimeException
{

    public QueueManagerException(String message)
    {
        super(message);
    }

    public QueueManagerException(String message, Throwable t)
    {
        super(message, t);
    }
}
