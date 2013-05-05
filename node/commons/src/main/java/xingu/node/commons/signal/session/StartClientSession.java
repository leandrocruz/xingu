package xingu.node.commons.signal.session;

import br.com.ibnetwork.xingu.crypto.SymmetricKey;

public class StartClientSession
    extends StartSession
{
    private byte[] sessionKeyBytes;
    
    StartClientSession()
    {}

    public StartClientSession(SymmetricKey sessionKey)
    {
        sessionKeyBytes = sessionKey.bytes();
    }

    public byte[] bytes()
    {
        return sessionKeyBytes;
    }
}