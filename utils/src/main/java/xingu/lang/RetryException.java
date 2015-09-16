package xingu.lang;

public class RetryException
    extends RuntimeException
{

    public RetryException()
    {
        super();
    }

    public RetryException(String message)
    {
        super(message);
    }

    public RetryException(Throwable cause)
    {
        super(cause);
    }

    public RetryException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
