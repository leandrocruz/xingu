package xingu.crypto;

import java.security.Key;
import java.security.PublicKey;

public interface PubKey
    extends PublicKey
{
    Key wrappedKey();
    
    long keyId();

    boolean isEncryptionKey();

    int bitStrength();

    String hash();
}
