package xingu.lang;

public class BadParameter
    extends RuntimeException
{

    public BadParameter()
    {
        super();
    }

    public BadParameter(String message)
    {
        super(message);
    }

    public BadParameter(Throwable cause)
    {
        super(cause);
    }

    public BadParameter(String message, Throwable cause)
    {
        super(message, cause);
    }

}
