package xingu.node.commons.signal.session;


public class SessionNotFound
    extends HandShakeSignal
{
    private String sessionId;
    
    SessionNotFound()
    {}
    
    public SessionNotFound(String sessionId)
    {
        this.sessionId = sessionId;
    }

    @Override
    public String getSessionId()
    {
        return sessionId;
    }

}
