package xingu.crypto.impl.pgp;

import java.io.IOException;
import java.security.Key;

import org.bouncycastle.bcpg.PublicKeyAlgorithmTags;
import org.bouncycastle.openpgp.PGPPublicKey;

import xingu.crypto.PubKey;
import xingu.lang.NotImplementedYet;

public class PGPPublicKeyWrapper
    implements PubKey
{
    private PGPPublicKey key;

    public PGPPublicKeyWrapper(PGPPublicKey key)
    {
        this.key = key;
    }
    
    public PGPPublicKey toPGP()
    {
        return key;
    }

    @Override
    public String getAlgorithm()
    {
        int algo = key.getAlgorithm();
        switch (algo)
        {
            case PublicKeyAlgorithmTags.RSA_ENCRYPT:
            case PublicKeyAlgorithmTags.RSA_GENERAL:
            case PublicKeyAlgorithmTags.RSA_SIGN:
                return "RSA";
            case PublicKeyAlgorithmTags.DSA:
                return "DSA";
            case PublicKeyAlgorithmTags.ELGAMAL_ENCRYPT:
            case PublicKeyAlgorithmTags.ELGAMAL_GENERAL:
                return "ElGamal";
            default:
                throw new NotImplementedYet();
        }
    }

    @Override
    public byte[] getEncoded()
    {
        try
        {
            return key.getEncoded();
        }
        catch (IOException e)
        {
            throw new NotImplementedYet();
        }
    }

    @Override
    public String getFormat()
    {
        throw new NotImplementedYet();
    }

    @Override
    public int bitStrength()
    {
        return key.getBitStrength();
    }

    @Override
    public long keyId()
    {
        return key.getKeyID();
    }

    @Override
    public boolean isEncryptionKey()
    {
        return key.isEncryptionKey();
    }

    @Override
    public Key wrappedKey()
    {
        throw new NotImplementedYet();
    }
    
    @Override
    public String hash()
    {
        throw new NotImplementedYet();
    }
}
