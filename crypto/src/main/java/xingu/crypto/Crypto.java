package xingu.crypto;

import java.io.InputStream;
import java.security.Key;
import java.security.PrivateKey;

public interface Crypto 
{
	PubKey readPublicKey(String file)
		throws Exception;
	
	PubKey readPublicKey(InputStream is)
    	throws Exception;
	
	PrivateKey readPrivateKey(String file, long id, String passphrase)
        throws Exception;

    PrivateKey readPrivateKey(InputStream is, long id, String passphrase)
        throws Exception;
	
	byte[] encrypt(InputStream is, Key publicKey)
    	throws Exception;

	byte[] decrypt(InputStream is, Key privateKey)
    	throws Exception;

	SymmetricKey readSymmetricKey(InputStream is)
	    throws Exception;
	
    SymmetricKey symmetricKey(String algorithm, int keySize)
        throws Exception;

}