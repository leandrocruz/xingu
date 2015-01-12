package xingu.node.commons.crypto.impl;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.security.PrivateKey;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.container.Environment;
import xingu.container.Inject;
import xingu.crypto.Crypto;
import xingu.crypto.PubKey;
import xingu.lang.NotImplementedYet;
import xingu.node.commons.crypto.KeyPair;
import xingu.utils.FSUtils;

public class KeyPairImpl
    implements KeyPair, Configurable, Initializable
{
    @Inject
    private Crypto crypto;

    @Inject
    private Environment env;
    
    private String id;

    private PubKey publicKey;

    private PrivateKey privateKey;

    private String publicKeyFile;

    private String secretKeyFile;

    private String privateKeyPassword;
    
    private static final Logger logger = LoggerFactory.getLogger(KeyPairImpl.class);

    @Override
    public void configure(Configuration conf)
        throws ConfigurationException
    {
        id = conf.getAttribute("id");
        publicKeyFile = env.replaceVars(conf.getChild("publicKey").getAttribute("file", null));
        secretKeyFile = env.replaceVars(conf.getChild("secretKey").getAttribute("file", null));
        privateKeyPassword = conf.getChild("privateKey").getAttribute("password", "");
    }
    
    @Override
    public void initialize()
        throws Exception
    {
//        if(findFile(publicKeyFile) == null)
//        {
//            logger.warn("Public key not found: {}", publicKeyFile);
//        }
//        if(findFile(secretKeyFile) == null)
//        {
//            logger.warn("Secret key not found: {}", secretKeyFile);
//        }
    }

    @Override
    public String id()
    {
        return id;
    }

    private InputStream toInputStream(String location)
    	throws Exception
	{
		if(location == null)
    	{
    		return null;
    	}

		URL url = Thread.currentThread().getContextClassLoader().getResource(location);
		if(url == null)
		{
			return null;
		}
		return url.openStream();
	}

    private PubKey loadPublicKey() 
        throws Exception
    {
    	InputStream is = toInputStream(publicKeyFile);
        if(is == null)
        {
            return null;
        }
        return crypto.readPublicKey(is);
    }

	private PrivateKey loadPrivateKey() 
        throws Exception
    {
    	InputStream is = toInputStream(secretKeyFile);
        if(is == null)
        {
            return null;
        }

        PubKey key = publicKey();
		long keyId = key == null ? -1 : key.keyId();
        return crypto.readPrivateKey(is, keyId, privateKeyPassword);
    }

    @Override
    public PrivateKey privateKey() 
        throws Exception
    {
        if (privateKey == null)
        {
            privateKey = loadPrivateKey();
        }
        return privateKey;
    }

    @Override
    public PubKey publicKey() 
        throws Exception
    {
        if (publicKey == null)
        {
            publicKey = loadPublicKey();
        }
        return publicKey;
    }

    private String findFile(String fileName)
    	throws Exception
    {
    	if(fileName == null)
    	{
    		return null;
    	}
    	if(fileName.startsWith("/"))
    	{
    		String path = FSUtils.loadFromClasspath(fileName);
    		if(path == null)
    		{
    			File file = new File(fileName);
    			if(file.exists())
    			{
    				path = file.getAbsolutePath();
    			}
    		}
    		return path;
    	}
    	else
    	{
    		throw new NotImplementedYet();
    	}
    }

    //@Override
    private void publicKey(PubKey key)
    {
        this.publicKey = key;
        try
        {
            String path = FSUtils.loadFromClasspath(publicKeyFile);
            if(path == null)
            {
                path = publicKeyFile;
            }
            File file = new File(path);
            byte[] encoded = key.getEncoded();
            FileUtils.writeByteArrayToFile(file, encoded);
        }
        catch(Exception e)
        {
            logger.warn("Could not write key to disk: "+publicKeyFile);
        }
    }
}