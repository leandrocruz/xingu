package xingu.crypto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

import xingu.container.Binder;
import xingu.crypto.Crypto;
import xingu.crypto.PubKey;
import xingu.crypto.impl.pgp.OpenPGPCrypto;
import xingu.utils.FSUtils;

public class OpenPGPCryptoTest
    extends CryptoTestSupport
{

    @Override
    protected void rebind(Binder binder)
    {
        binder.bind(Crypto.class).to(OpenPGPCrypto.class);
    }

    @Override
    protected Map<String, Integer> algorithms()
    {
        Map<String, Integer> keySizeByAlgorithm = new HashMap<String, Integer>();
        int SIZE = 128; //doesn't require unlimited JCE
        keySizeByAlgorithm.put("AES", SIZE);
        keySizeByAlgorithm.put("Twofish", SIZE);
        keySizeByAlgorithm.put("Blowfish", SIZE);
        keySizeByAlgorithm.put("Serpent", SIZE);
        keySizeByAlgorithm.put("DES", 56);
        return keySizeByAlgorithm;
    }

    @Override
    protected String encryptedFile()
    {
        return "crypto/file.txt.gpg";
    }

    @Override
    protected String loadPublicKeyFile()
    {
        return FSUtils.load("crypto/public.asc");
    }

    @Override
    protected String loadPrivateKeyFile()
    {
        return FSUtils.load("crypto/private.asc");
    }

    @Override
    protected void testPublicKey(PubKey publicKey)
    {
        assertEquals("A678D5DED96CCEFB", Long.toHexString(publicKey.keyId()).toUpperCase());
        assertEquals(2048, publicKey.bitStrength());
        assertEquals("ElGamal", publicKey.getAlgorithm());
        assertTrue(publicKey.isEncryptionKey());
    }

    @Override
    protected void testPrivateKey(PrivateKey privateKey)
    {
        assertEquals("ElGamal", privateKey.getAlgorithm());
    }

//  @Test
//  @Ignore
//  public void testArmor()
//      throws Exception
//  {
//      InputStream is;
//      File file;
//      String original;
//      String armor;
//      
//      //PUBLIC
//      file = FSUtils.loadAsFile("crypto/public.asc");
//      is = new FileInputStream(file);
//      is = PGPUtil.getDecoderStream(is);
//      armor = crypto.armor(new PGPPubKeyRingCollection(is));
//      original = FileUtils.readFileToString(file);
//      assertEquals(original, armor);
//
//      //SECRET/PRIVATE
//      file = FSUtils.loadAsFile("crypto/private.asc");
//      is = new FileInputStream(file);
//      is = PGPUtil.getDecoderStream(is);
//      armor = crypto.armor(new PGPSecretKeyRingCollection(is));
//      original = FileUtils.readFileToString(file);
//      assertEquals(original, armor);
//  }

}
