package xingu.exception.impl;


public class ExceptionWithData
    extends Exception
{
    private Object data;
    
    public ExceptionWithData(Throwable t)
    {
        super(t);
    }

    public ExceptionWithData(Throwable t, Object data)
    {
        super(t);
        this.data = data;
    }

    public Object data()
    {
        return data;
    }
}
