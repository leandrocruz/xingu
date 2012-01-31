package br.com.ibnetwork.xingu.crypto;

import static org.junit.Assert.assertEquals;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

import br.com.ibnetwork.xingu.container.Binder;
import br.com.ibnetwork.xingu.crypto.impl.rsa.RSACrypto;
import br.com.ibnetwork.xingu.utils.FSUtils;

public class RSACryptoTest
    extends CryptoTestSupport
{

    @Override
    protected void rebind(Binder binder)
    {
        binder.bind(Crypto.class).to(RSACrypto.class);
    }

    @Override
    protected Map<String, Integer> algorithms()
    {
        Map<String, Integer> keySizeByAlgorithm = new HashMap<String, Integer>();
        int SIZE = 128; //doesn't require unlimited JCE
        keySizeByAlgorithm.put("AES", SIZE);
        keySizeByAlgorithm.put("DES", 56);
        return keySizeByAlgorithm;
    }

    @Override
    protected String loadPrivateKeyFile()
    {
        return FSUtils.load("crypto/rsa/privateRSAKey.rsa");
    }

    @Override
    protected String loadPublicKeyFile()
    {
        return FSUtils.load("crypto/rsa/publicRSAKey.rsa");
    }

    @Override
    protected void testPrivateKey(PrivateKey privateKey)
    {
        assertEquals("RSA", privateKey.getAlgorithm());
    }

    @Override
    protected void testPublicKey(PubKey publicKey)
    {
        assertEquals("RSA", publicKey.getAlgorithm());
    }

    @Override
    protected String encryptedFile()
    {
        return "crypto/rsa/file.enc";
    }
}
