package xingu.node.commons.signal.session;

public class SessionAccepted
    extends HandShakeSignal
{
    private String sessionId;
    
    private boolean certificateAccepted;
    
    private long serverTime;
    
    public SessionAccepted()
    {}

    public SessionAccepted(boolean certificateAccepted, String sessionId)
    {
        this.sessionId = sessionId;
        this.certificateAccepted = certificateAccepted;
        this.serverTime = System.currentTimeMillis();
    }

    @Override
    public String getSessionId()
    {
        return sessionId;
    }
    
    public boolean certificateAccepted()
    {
        return certificateAccepted;
    }
    
    public long serverTime()
    {
        return serverTime;
    }
}
