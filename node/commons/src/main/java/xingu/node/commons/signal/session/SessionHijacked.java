package xingu.node.commons.signal.session;


public class SessionHijacked
    extends HandShakeSignal
{
    private String message;
    
    public SessionHijacked(String message)
    {
        this.message = message;
    }
    
    public String message()
    {
        return message;
    }
}
