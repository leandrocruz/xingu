package br.com.ibnetwork.xingu.crypto;

import static org.junit.Assert.assertEquals;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

import org.junit.Test;

import br.com.ibnetwork.xingu.crypto.impl.rsa.RSAUtils;

public class KeySizeTest
{
//    static 
//    {
//        Security.addProvider(new BouncyCastleProvider());
//    }

    private static final String algorithm = "RSA";
    
    @Test
    public void testRSAKeySize()
        throws Exception
    {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();

        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();
        performTest(privateKey, publicKey);
        
//        System.out.println("Private Key "+privateKey.getFormat()+"/"+privateKey.getAlgorithm());
//        System.out.println("Public Key "+publicKey.getFormat()+"/"+publicKey.getAlgorithm());
        

        //dump the key to disk and try to use it
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PrivateKey privateKey2 = RSAUtils.clone(keyFactory, privateKey);
        PublicKey publicKey2 = RSAUtils.clone(keyFactory, publicKey);

        performTest(privateKey2, publicKey);
        performTest(privateKey, publicKey2);
        performTest(privateKey2, publicKey2);
    }

    private void performTest(PrivateKey privateKey, PublicKey publicKey) 
        throws Exception
    {
        String message = "this is a message";
        byte[] cipherText = enc(message, privateKey);
        byte[] decrypted = dec(cipherText, publicKey);
        assertEquals(message, new String(decrypted));
    }

    private byte[] dec(byte[] cipherText, PublicKey publicKey)
        throws Exception
    {
        Cipher dec = Cipher.getInstance(algorithm);
        dec.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] decrypted = dec.doFinal(cipherText);
        return decrypted;
    }
    
    private byte[] enc(String message, Key privateKey)
        throws Exception
    {
        Cipher enc = Cipher.getInstance(algorithm);
        enc.init(Cipher.ENCRYPT_MODE, privateKey);
        return enc.doFinal(message.getBytes());
    }
}
