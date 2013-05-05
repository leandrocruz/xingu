package xingu.node.commons.signal.session;


public class SessionRejected
    extends HandShakeSignal
{
    private String message;
    
    SessionRejected()
    {}
    
    public SessionRejected(String message)
    {
        this.message = message;
    }
    
    public String message()
    {
        return message;
    }

    @Override
    public String toString()
    {
        return message;
    }
}
