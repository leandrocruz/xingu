package xingu.lang;

public class OperationUnsuccessfulException
    extends RuntimeException
{
    public OperationUnsuccessfulException()
    {
        super();
    }

    public OperationUnsuccessfulException(String message)
    {
        super(message);
    }

    public OperationUnsuccessfulException(Throwable cause)
    {
        super(cause);
    }

    public OperationUnsuccessfulException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
