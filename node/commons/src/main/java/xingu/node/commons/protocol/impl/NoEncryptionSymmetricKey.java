package xingu.node.commons.protocol.impl;

import br.com.ibnetwork.xingu.crypto.SymmetricKey;

public class NoEncryptionSymmetricKey
    implements SymmetricKey
{
    @Override
    public String algorithm()
    {
        return "NoEncryptionSymmetricKey";
    }

    @Override
    public byte[] bytes()
    {
        return null;
    }

    @Override
    public byte[] decrypt(byte[] bytes) 
        throws Exception
    {
        return bytes;
    }

    @Override
    public byte[] encrypt(String input) 
        throws Exception
    {
        return input.getBytes();
    }

    @Override
    public byte[] encrypt(byte[] bytes) 
        throws Exception
    {
        return bytes;
    }
}
