package br.com.ibnetwork.xingu.crypto.impl;

import java.io.InputStream;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import br.com.ibnetwork.xingu.crypto.Crypto;
import br.com.ibnetwork.xingu.crypto.SymmetricKey;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public abstract class CryptoSupport
    implements Crypto
{
   
    @Override
    public SymmetricKey readSymmetricKey(InputStream is) 
        throws Exception
    {
        throw new NotImplementedYet();
    }

    /**
     * See:
     * http://www.daemonology.net/blog/2009-06-11-cryptographic-right-answers.html
     * http://www.java2s.com/Code/Java/Security/BasicIOexamplewithCTRusingAES.htm
     */
    @Override
    public SymmetricKey symmetricKey(String algorithm, int keySize)
            throws Exception
    {
        String algorithmModePadding = algorithm;
        int index = algorithm.indexOf("/");
        if(index > 0)
        {
            algorithm = algorithm.substring(0, index);
        }
        KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
        keyGen.init(keySize);
        SecretKey key = keyGen.generateKey();
        SymmetricKeyImpl result = new SymmetricKeyImpl(key, algorithmModePadding); 
        return result;
    }
}
