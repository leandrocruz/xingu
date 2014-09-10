package xingu.node.commons.crypto.impl;

import java.io.File;
import java.security.PrivateKey;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.node.commons.crypto.KeyPair;
import br.com.ibnetwork.xingu.container.Environment;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.crypto.Crypto;
import br.com.ibnetwork.xingu.crypto.PubKey;
import br.com.ibnetwork.xingu.utils.FSUtils;

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
        if(findFile(publicKeyFile) == null)
        {
            logger.warn("Public key not found: {}", publicKeyFile);
        }
        if(findFile(secretKeyFile) == null)
        {
            logger.warn("Secret key not found: {}", secretKeyFile);
        }
    }

    @Override
    public String id()
    {
        return id;
    }

    private PubKey loadPublicKey() 
        throws Exception
    {
    	if(publicKeyFile == null)
    	{
    		return null;
    	}

        String path = findFile(publicKeyFile);
        if(path == null)
        {
            return null;
        }
        return crypto.readPublicKey(path);
    }

    private PrivateKey loadPrivateKey() 
        throws Exception
    {
        String path = findFile(secretKeyFile);
        if(path == null)
        {
            return null;
        }
        PubKey key = publicKey();
		long keyId = key == null ? -1 : key.keyId();
        return crypto.readPrivateKey(path, keyId, privateKeyPassword);
    }
    
    private String findFile(String fileName)
    {
    	if(fileName == null)
    	{
    		return null;
    	}
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
    
    @Override
    public void publicKey(PubKey key)
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