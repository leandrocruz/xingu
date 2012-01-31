package br.com.ibnetwork.xingu.crypto.impl.rsa;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

import org.apache.commons.io.IOUtils;

import br.com.ibnetwork.xingu.crypto.PubKey;

public class RSAUtils
{
    private static final String ALGORITHM = "RSA";
    
    //X509
    public static PublicKey clone(KeyFactory keyFactory, PublicKey key)
        throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        dump(key, baos);
        PublicKey result = keyFactory.generatePublic(new X509EncodedKeySpec(baos.toByteArray()));
        return result;
    }

    //PKCS8
    public static PrivateKey clone(KeyFactory keyFactory, PrivateKey key)
        throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        dump(key, baos);
        PrivateKey result = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(baos.toByteArray()));
        return result;
    }
    
    public static void dump(PrivateKey key, OutputStream os)
        throws Exception
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(new PKCS8EncodedKeySpec(key.getEncoded()).getEncoded());
        IOUtils.copy(bais, os);
    }
    
    public static void dump(PublicKey key, OutputStream os)
        throws Exception
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(new X509EncodedKeySpec(key.getEncoded()).getEncoded());
        IOUtils.copy(bais, os);
    }

    public static byte[] decrypt(InputStream is, Key key)
        throws Exception
    {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        CipherInputStream cis = new CipherInputStream(is, cipher);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        IOUtils.copy(cis, baos);
        byte[] decrypted = baos.toByteArray();
        return decrypted;
    }

    public static byte[] encrypt(InputStream is, Key key)
        throws Exception
    {
        if(key instanceof PubKey)
        {
            key = ((PubKey) key).wrappedKey();
        }
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CipherOutputStream cos = new CipherOutputStream(baos, cipher);
        IOUtils.copy(is, cos);
        cos.close();
        byte[] cipherText = baos.toByteArray();
        return cipherText;
    }
}
