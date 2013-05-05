package xingu.node.commons.signal.session;

import xingu.node.commons.signal.SignalSupport;

public class ServerPublicKey
    extends SignalSupport
{
    private byte[] bytes;

    public ServerPublicKey(byte[] bytes)
    {
        this.bytes = bytes;
    }

    public byte[] bytes()
    {
        return bytes;
    }
}