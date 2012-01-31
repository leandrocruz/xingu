package br.com.ibnetwork.xingu.crypto.impl.pgp;

import java.security.PrivateKey;

import org.bouncycastle.openpgp.PGPPrivateKey;

public class PGPPrivateKeyWrapper
    implements PrivateKey
{
    private PGPPrivateKey key;

    public PGPPrivateKeyWrapper(PGPPrivateKey key)
    {
        this.key = key;
    }
    
    public PGPPrivateKey toPGP()
    {
        return key;
    }

    @Override
    public String getAlgorithm()
    {
        return key.getKey().getAlgorithm();
    }

    @Override
    public byte[] getEncoded()
    {
        return key.getKey().getEncoded();
    }

    @Override
    public String getFormat()
    {
        return key.getKey().getFormat();
    }

}
