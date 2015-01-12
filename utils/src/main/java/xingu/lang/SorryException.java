package xingu.lang;

public class SorryException
    extends RuntimeException
{

    public SorryException()
    {
        super();
    }

    public SorryException(String message)
    {
        super(message);
    }

    public SorryException(Throwable cause)
    {
        super(cause);
    }

    public SorryException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
