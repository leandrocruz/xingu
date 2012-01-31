package br.com.ibnetwork.xingu.crypto.impl.pgp;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;

import br.com.ibnetwork.xingu.crypto.PubKey;
import br.com.ibnetwork.xingu.crypto.impl.CryptoSupport;


public class OpenPGPCrypto 
    extends CryptoSupport
{
    static 
    {
        Security.addProvider(new BouncyCastleProvider());
    }
    
    @Override
    public PrivateKey readPrivateKey(String file, long id, String passphrase) 
        throws Exception
    {
        InputStream is = new FileInputStream(file);
        return readPrivateKey(is, id, passphrase);
    }

    @Override
    public PrivateKey readPrivateKey(InputStream is, long id, String passphrase) 
        throws Exception
    {
        PGPSecretKey secretKey = pgpSecretKey(is, id);
        PGPPrivateKey privateKey = secretKey.extractPrivateKey(passphrase.toCharArray(), "BC");
        return new PGPPrivateKeyWrapper(privateKey);
    }

    @SuppressWarnings("unchecked")
    private PGPSecretKey pgpSecretKey(InputStream is, long id)
        throws Exception
    {
        PGPSecretKeyRingCollection ringCollection = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(is));
        Iterator<PGPSecretKeyRing> it = ringCollection.getKeyRings();
        while (it.hasNext())
        {
            PGPSecretKeyRing ring = (PGPSecretKeyRing) it.next();
            Iterator iterator = ring.getSecretKeys();
            while(iterator.hasNext())
            {
                PGPSecretKey key = (PGPSecretKey) iterator.next();
                if(key.getKeyID() == id)
                {
                    return key;
                }
            }
        }
        throw new IllegalArgumentException("Can't find key ["+id+"] in key ring.");
    }

    @Override
    public PubKey readPublicKey(String file)
        throws Exception
    {
        InputStream is = new FileInputStream(file);
        return readPublicKey(is);
    }

    @SuppressWarnings("unchecked")
    @Override
    public PubKey readPublicKey(InputStream is)
        throws Exception
    {
        PGPPublicKeyRingCollection collection = new PGPPublicKeyRingCollection(PGPUtil.getDecoderStream(is));
        Iterator<PGPPublicKeyRing> it = collection.getKeyRings();
        while (it.hasNext())
        {
            PGPPublicKeyRing ring = it.next();
            Iterator<PGPPublicKey> iterator = ring.getPublicKeys();
            while (iterator.hasNext())
            {
                PGPPublicKey k = iterator.next();
                if (k.isEncryptionKey())
                {
                    return new PGPPublicKeyWrapper(k);
                }
            }
        }
        throw new IllegalArgumentException("Can't find encryption key in key ring.");
    }

    @Override
    public byte[] encrypt(InputStream is, Key publicKey)
        throws Exception
    {
    	//write the literal data packet
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	PGPCompressedDataGenerator compressedData = new PGPCompressedDataGenerator(PGPCompressedData.ZIP);
    	writeFileToLiteralData(is, compressedData.open(baos), PGPLiteralData.BINARY);
    	compressedData.close();
    	
    	PGPEncryptedDataGenerator encryptedData = new PGPEncryptedDataGenerator(PGPEncryptedData.CAST5, true, new SecureRandom(), "BC");
    	PGPPublicKey wrapped = ((PGPPublicKeyWrapper) publicKey).toPGP();
    	encryptedData.addMethod(wrapped);
    	
    	//encrypt 
    	byte[] bytes = baos.toByteArray();
    	ByteArrayOutputStream out = new ByteArrayOutputStream(bytes.length);
    	OutputStream tmp = encryptedData.open(out, bytes.length);
    	tmp.write(bytes);
    	
    	//cleanup
    	tmp.close();
    	out.close();
    	return out.toByteArray();
    }

    private void writeFileToLiteralData(InputStream is, OutputStream out, char type) 
    	throws Exception
    {
    	byte[] buffer = IOUtils.toByteArray(is);
        PGPLiteralDataGenerator data = new PGPLiteralDataGenerator();
        OutputStream tmp = data.open(out, type, "some file name", buffer.length, new Date());
        IOUtils.write(buffer, tmp);
        data.close();
        is.close();
	}

	/**
     * Decrypts the message from InputStream to OutputStrem using the PGPPrivateKey
     */
    @SuppressWarnings("unchecked")
    @Override
    public byte[] decrypt(InputStream is, Key key)
        throws Exception
    {
        PGPEncryptedDataList dataList = getDataList(is);
        Iterator<PGPPublicKeyEncryptedData> it = dataList.getEncryptedDataObjects();
        PGPPublicKeyEncryptedData data = null;
        PGPPrivateKey wrapped = ((PGPPrivateKeyWrapper) key).toPGP();
        while (it.hasNext())
        {
            data = it.next();
            if(wrapped.getKeyID() != data.getKeyID())
            {
                throw new Exception("Message was encrypted with another key");
            }
        }

        Object obj = objectFromStream(wrapped, data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (obj instanceof PGPLiteralData)
        {
            PGPLiteralData ld = (PGPLiteralData) obj;
            
            IOUtils.copy(ld.getInputStream(), baos);
        }
        else if (obj instanceof PGPOnePassSignatureList)
        {
            throw new PGPException("encrypted message contains a signed message - not literal data.");
        }
        else
        {
            throw new PGPException("message is not a simple encrypted file - type unknown.");
        }
        checkIntegrity(data);
        return baos.toByteArray();
    }

    private static PGPEncryptedDataList getDataList(InputStream is)
            throws IOException
    {
        PGPObjectFactory factory = new PGPObjectFactory(PGPUtil.getDecoderStream(is));
        Object obj = factory.nextObject();
        if (obj instanceof PGPEncryptedDataList)
        {
            return (PGPEncryptedDataList) obj;
        }
        else
        {
            // the first object might be a PGP marker packet.
            return (PGPEncryptedDataList) factory.nextObject();
        }
    }

    private static Object objectFromStream(PGPPrivateKey key, PGPPublicKeyEncryptedData pbe) 
        throws Exception
    {
        PGPObjectFactory factory = new PGPObjectFactory(pbe.getDataStream(key, "BC"));
        Object result = factory.nextObject();
        if (result instanceof PGPCompressedData)
        {
            PGPCompressedData compressed = (PGPCompressedData) result;
            PGPObjectFactory pgpFact = new PGPObjectFactory(compressed.getDataStream());
            result = pgpFact.nextObject();
        }
        return result;
    }

    private static void checkIntegrity(PGPPublicKeyEncryptedData encrypted)
        throws Exception
    {
        if (encrypted.isIntegrityProtected())
        {
            if (!encrypted.verify())
            {
                System.err.println("message failed integrity check");
            }
        }
        else
        {
            System.err.println("no message integrity check");
        }
    }

    
    public String armor(PGPPublicKeyRingCollection ringCollection)
        throws Exception
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ringCollection.encode(new ArmoredOutputStream(os));
        os.close();
        String result = new String(os.toByteArray());
        return result;
    }

    
    public String armor(PGPSecretKeyRingCollection ringCollection)
        throws Exception
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ringCollection.encode(new ArmoredOutputStream(os));
        os.close();
        String result = new String(os.toByteArray());
        return result;
    }
}