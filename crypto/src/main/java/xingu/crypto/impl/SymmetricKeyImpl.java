package xingu.crypto.impl;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import xingu.crypto.SymmetricKey;
import xingu.utils.HexUtils;
import xingu.utils.StringUtils;

public class SymmetricKeyImpl
    implements SymmetricKey
{
    private final SecretKey key;
    
    private final Cipher encCipher;
    
    private final Cipher decCipher;
    
    public SymmetricKeyImpl(byte[] keyBytes, String algorithm)
    {
        this(new SecretKeySpec(keyBytes, algorithm), algorithm);
    }

    public SymmetricKeyImpl(SecretKey key, String algorithm)
    {
        this.key = key;
        try
        {
            encCipher = Cipher.getInstance(algorithm); 
            encCipher.init(Cipher.ENCRYPT_MODE, key);
        
            decCipher = Cipher.getInstance(algorithm);
            decCipher.init(Cipher.DECRYPT_MODE, key);
        }
        catch(Throwable t)
        {
            throw new RuntimeException("Error initializing key: " + algorithm, t);
        }
    }

    @Override
    public String algorithm()
    {
        return key.getAlgorithm();
    }

    @Override
    public byte[] bytes()
    {
        return key.getEncoded();
    }

    @Override
    public byte[] encrypt(String input) 
        throws Exception
    {
        return encrypt(input.getBytes("UTF8"));
    }

    @Override
    public byte[] encrypt(byte[] bytes)
        throws Exception
    {
        byte[] raw = encCipher.doFinal(bytes);
        return raw;
    }

    @Override
    public byte[] decrypt(byte[] bytes)
        throws Exception
    {
        byte[] raw = decCipher.doFinal(bytes);
        return raw;
    }

    @Override
    public String toString()
    {
        return algorithm()+"/"+HexUtils.toHex(bytes());
    }
}