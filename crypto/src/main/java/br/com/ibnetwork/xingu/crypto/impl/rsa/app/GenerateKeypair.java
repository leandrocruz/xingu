package br.com.ibnetwork.xingu.crypto.impl.rsa.app;

import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import br.com.ibnetwork.xingu.crypto.impl.rsa.RSAUtils;

public class GenerateKeypair
    implements Command
{
    private int length;
    
    public GenerateKeypair(int length)
    {
        this.length = length;
    }

    @Override
    public Object exec()
        throws Exception
    {
        System.out.println("Generating key pair with "+length+" ...");
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(length);
        KeyPair pair = keyGen.generateKeyPair();

        File file = File.createTempFile("privateRSAKey-", ".rsa");
        PrivateKey privateKey = pair.getPrivate();
        RSAUtils.dump(privateKey, new FileOutputStream(file));
        System.out.println("Private Key written to: "+ file);
        
        file = File.createTempFile("publicRSAKey-", ".rsa");
        PublicKey publicKey = pair.getPublic();
        RSAUtils.dump(publicKey, new FileOutputStream(file));
        System.out.println("Public Key written to: "+ file);
        return null;
    }

}
