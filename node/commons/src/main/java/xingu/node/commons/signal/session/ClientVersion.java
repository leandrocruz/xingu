package xingu.node.commons.signal.session;

public class ClientVersion
    extends HandShakeSignal
{
    private String versionString;
    
    public ClientVersion(String versionString)
    {
        this.versionString = versionString;
    }

    public String version()
    {
        return versionString;
    }
}
