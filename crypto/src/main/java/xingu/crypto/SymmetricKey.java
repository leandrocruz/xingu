package xingu.crypto;

public interface SymmetricKey
{
    byte[] bytes();

    String algorithm();

    byte[] encrypt(String input) 
        throws Exception;
    
    byte[] encrypt(byte[] bytes) 
        throws Exception;

    byte[] decrypt(byte[] bytes) 
        throws Exception;
}
