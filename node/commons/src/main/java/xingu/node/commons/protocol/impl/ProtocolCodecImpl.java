package xingu.node.commons.protocol.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.Key;

import org.apache.commons.io.IOUtils;

import xingu.node.commons.crypto.KeyManager;
import xingu.node.commons.crypto.SessionKeyHolder;
import xingu.node.commons.protocol.ProtocolCodec;

import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.crypto.Crypto;
import br.com.ibnetwork.xingu.crypto.PubKey;
import br.com.ibnetwork.xingu.crypto.SymmetricKey;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class ProtocolCodecImpl
	implements ProtocolCodec
{
	private static final int PLAIN_TEXT = 1;

	private static final int ENCRYPTED_WITH_PUBLIC_KEY = 2;
  
	private static final int ENCRYPTED_WITH_SESSION_KEY = 3;

    @Inject
    private Crypto crypto;
    
    @Inject
    private KeyManager keyManager;

    @Inject
    private SessionKeyHolder keyHolder;

	@Override
	public String decode(long keyId, byte[] bytes, int type)
		throws Exception
	{
        byte[] decoded = null;
        switch (type)
        {
            case PLAIN_TEXT:
                decoded = bytes;
                break;


            case ENCRYPTED_WITH_SESSION_KEY:
                SymmetricKey key = keyHolder.symmetricKey(keyId);
                if(key == null)
                {
                    throw new Exception("Can't find symmetric key '"+keyId+"'");
                }
                decoded = key.decrypt(bytes);
                break;


            case ENCRYPTED_WITH_PUBLIC_KEY:
                Key privateKey = keyManager.pair("protocol").privateKey();
                InputStream is = new ByteArrayInputStream(bytes);
                decoded = crypto.decrypt(is, privateKey);
                IOUtils.closeQuietly(is);
                if(decoded.length == 0)
                {
                    throw new Exception("Can't decrypt your message. are you sure you are using the correct RSA key pair ?");
                }
                break;


            default:
                throw new Exception("Unknown message type: "+type);
        }
        return new String(decoded);
	}

	@Override
	public byte[] encode(long keyId, String input, int type)
		throws Exception
	{
        switch (type)
        {
            case PLAIN_TEXT:
                return input.getBytes("UTF8");

            case ENCRYPTED_WITH_PUBLIC_KEY:
                PubKey pkey = keyManager.pair("protocol").publicKey();
                InputStream is = IOUtils.toInputStream(input);
                byte[] data = crypto.encrypt(is, pkey);
                IOUtils.closeQuietly(is);
                if(data == null || data.length == 0)
                {
                    throw new Exception("Failed to encrypt input with public key"); 
                }
                return data;

            case ENCRYPTED_WITH_SESSION_KEY:
                SymmetricKey key = keyHolder.symmetricKey(keyId);
                return key.encrypt(input);

            default:
                throw new NotImplementedYet("Unknown message type: "+ type);
        }
	}

	@Override
	public int typeFrom(Object obj)
	{
        if(usePlainText(obj))
        {
            return PLAIN_TEXT;
        }
        else if(useAsymmetricEncryption(obj))
        {
            return ENCRYPTED_WITH_PUBLIC_KEY;
        }
        else
        {
            return ENCRYPTED_WITH_SESSION_KEY;
        }
	}

	private boolean usePlainText(Object obj)
	{
		return true;
	}

	private boolean useAsymmetricEncryption(Object obj)
	{
		return true;
	}

}