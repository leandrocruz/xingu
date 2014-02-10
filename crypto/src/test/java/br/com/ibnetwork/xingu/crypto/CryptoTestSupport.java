package br.com.ibnetwork.xingu.crypto;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.Provider.Service;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Ignore;
import org.junit.Test;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.container.XinguTestCase;
import br.com.ibnetwork.xingu.utils.FSUtils;

public abstract class CryptoTestSupport
	extends XinguTestCase
{
	@Inject
	protected Crypto crypto;
	
	@Test
    @Ignore
	public void printProvidersAndServices() 
    {
        Security.addProvider(new BouncyCastleProvider());
        HashSet<String> serviceTypes = new HashSet<String>();

        Provider[] providers = Security.getProviders();
        for (Provider provider : providers) 
        {
                System.out.println();
                System.out.println("Provider: " + provider.getName() + " (" + provider + ")");
                Set<Service> services = provider.getServices();
                for (Service service : services) 
                {
                        String type = service.getType();
                        serviceTypes.add(type);
                        System.out.println("\n\tType: " + type);
                        System.out.println("\tAlgo: " + service.getAlgorithm());
                }
        }

        for (String type : serviceTypes) 
        {
                System.out.println("\nService Type: " + type);
                for (String alg : Security.getAlgorithms(type)) 
                {
                        System.out.println(alg);
                }
        }
    }

    @Test
    public void testReadKeys()
        throws Exception
    {
        String publicKeyFile = loadPublicKeyFile();
        PubKey publicKey = crypto.readPublicKey(publicKeyFile);
        testPublicKey(publicKey);
        
        String privateKeyFile = loadPrivateKeyFile();
        PrivateKey privateKey = crypto.readPrivateKey(privateKeyFile, publicKey.keyId(), "didimoco");
        testPrivateKey(privateKey);
    }

    protected abstract void testPublicKey(PubKey publicKey);
    
    protected abstract void testPrivateKey(PrivateKey privateKey);

    protected abstract String loadPublicKeyFile();
    
    protected abstract String loadPrivateKeyFile();

//    @Test
//    @Ignore
//    public void testDecrypt()
//        throws Exception
//    {
//        PubKey publicKey = crypto.readPublicKey(loadPublicKeyFile());
//        PrivateKey privateKey = crypto.readPrivateKey(loadPrivateKeyFile(), publicKey.keyId(), "didimoco");
//        
//        String expected = FileUtils.readFileToString(FSUtils.loadAsFile("crypto/file.txt"));
//        byte[] text;
//        
//        text = decrypt(privateKey, encryptedFile());
//        assertEquals(expected, text);
//
//        text = decrypt(privateKey, "crypto/file.txt.asc");
//        assertEquals(expected, text);
//
//    }

    protected abstract String encryptedFile();

    @Test
    public void testEncryptAndDecrypt()
        throws Exception
    {
        String input = "crypto/file.txt"; // only works with a small input. One block of data
        PubKey publicKey = crypto.readPublicKey(loadPublicKeyFile());
        PrivateKey privateKey = crypto.readPrivateKey(loadPrivateKeyFile(), publicKey.keyId(), "didimoco");

        byte[] encrypted = crypto.encrypt(FSUtils.toInputStream(input), publicKey);
        byte[] clearText = crypto.decrypt(new ByteArrayInputStream(encrypted), privateKey);

        String expected = FSUtils.readFile(input);
        assertEquals(expected, new String(clearText));
    }
 
    @Test
    public void testSymmetricKey()
        throws Exception
    {
        Map<String, Integer> keySizeByAlgorithm = algorithms();
        
        //"AES/CTR/NoPadding"
        String file = FSUtils.load("lipsum.txt");
        String input = FileUtils.readFileToString(new File(file));
        for (String algorithm : keySizeByAlgorithm.keySet())
        {
            int keySize = keySizeByAlgorithm.get(algorithm);
            SymmetricKey key = crypto.symmetricKey(algorithm, keySize);
            long start = System.currentTimeMillis();
            for (int i=0 ; i<200 ; i++)
            {
                byte[] encrypted = key.encrypt(input);
                byte[] decrypted = key.decrypt(encrypted);
                assertEquals(input, new String(decrypted));
            }
            System.out.println("Algorithm: "+key.algorithm()+"/"+keySize+" time: "+(System.currentTimeMillis() - start)+"ms");
        }
    }
    
    protected abstract Map<String, Integer> algorithms();

    @Test
    public void testEncryptSymmetricKey()
        throws Exception
    {
        SymmetricKey symmetric = crypto.symmetricKey("AES", 128);
        byte[] bytes = symmetric.bytes();
        InputStream is = new ByteArrayInputStream(bytes);
        PubKey publicKey = crypto.readPublicKey(loadPublicKeyFile());
        PrivateKey privateKey = crypto.readPrivateKey(loadPrivateKeyFile(), publicKey.keyId(), "didimoco");

        byte[] encrypted = crypto.encrypt(is, publicKey);
        byte[] clearText = crypto.decrypt(new ByteArrayInputStream(encrypted), privateKey);

        for(int i=0 ; i<bytes.length ; i++)
        {
            assertEquals(bytes[i], clearText[i]);
        }
    }
}
