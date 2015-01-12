package br.com.ibnetwork.xingu.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.IOUtils;

public class MD5Utils
{
    /**
     * Take a string and return its md5 hash as a hex digit string
     * or null if an error occurs
     */
    public static String md5Hash(String arg)
    {
        return md5Hash(arg.getBytes());
    }

    /**
     * Take a byte array and return its md5 hash as a hex digit string
     * or null if an error occurs
     */
    public static String md5Hash(byte barray[])
    {   
        String restring = null;
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(barray);
            byte[] result = md.digest();            
            restring = HexUtils.toHex(result);
        }  
        catch (NoSuchAlgorithmException ignored)
        { 
            //return null
            return null;
        } 
        
        return restring;
    }

	public static String md5Hash(File file)
		throws IOException
	{
		InputStream is = null;
		try
		{
			is = new FileInputStream(file);
			byte[] bytes = IOUtils.toByteArray(is);
			return md5Hash(bytes);
		}
		finally
		{
			IOUtils.closeQuietly(is);
		}
	}
}
