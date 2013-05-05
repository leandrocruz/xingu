package xingu.node.commons.crypto;

import java.security.PrivateKey;

import br.com.ibnetwork.xingu.crypto.PubKey;

public interface KeyPair
{
    String id();

    PubKey publicKey()
        throws Exception;

    void publicKey(PubKey pubKey);
    
    PrivateKey privateKey() 
        throws Exception;
}
