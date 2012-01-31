package br.com.ibnetwork.xingu.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
            restring = StringUtils.toHex(result);
        }  
        catch (NoSuchAlgorithmException ignored)
        { 
            //return null
            return null;
        } 
        
        return restring;
    }


}
