package br.com.ibnetwork.xingu.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class FSUtils
{
    public static final File clone(File file) 
        throws IOException
    {
        File folder = new File(file.getParent() + File.separator + "clone");
        folder.mkdirs();
        return clone(file,folder);
    }
    
    public static final File clone(File file, File target) 
        throws IOException
    {
        File clone = new File(target + File.separator + file.getName());
        if(clone.exists())
        {
            throw new IOException("File already exists: "+clone);
        }
        if(file.isFile())
        {
            FileUtils.copyFile(file,clone);
        }
        else
        {
            clone.mkdirs();
            File[] children = file.listFiles();
            for (int i = 0; i < children.length; i++)
            {
                File child = children[i];
                clone(child,clone);
            }
        }
        return clone;
    }

    public static final boolean compare(File f1, File f2) 
        throws IOException
    {
        if(f1.isFile() && f2.isFile())
        {
            return compareFiles(f1,f2,"ISO-8859-1");
        }
        else if(f1.isDirectory() && f2.isDirectory())
        {
            return compareFolders(f1,f2);
        }
        return false;
    }

    private static final boolean compareFiles(File f1, File f2, String charset) 
        throws IOException
    {
        String s1 = FileUtils.readFileToString(f1,charset);
        String s2 = FileUtils.readFileToString(f2,charset);
        boolean result = s1.equals(s2);
        return result;
    }
    
    private static final boolean compareFolders(File f1, File f2) 
        throws IOException
    {
        //check folder names
        if(!f1.getName().equals(f2.getName()))
        {
            return false;
        }
        
        //check child count
        File[] c1 = f1.listFiles();
        File[] c2 = f2.listFiles();
        if(c1.length != c2.length)
        {
            return false;
        }
        
        //recurse children
        for (int i = 0; i < c1.length; i++)
        {
            File tmp1 = c1[i];
            File tmp2 = c2[i];
            if(!compare(tmp1,tmp2))
            {
                return false;
            }
        }
        return true;
    }
    
    public static final File loadAsFile(String name)
    {
        String path = FSUtils.load(name);
        return path == null ? null: new File(path);
    }

    public static String load(String name)
    {
        String prefix = FilenameUtils.getPrefix(name);
        if(prefix.length() > 0)
        {
            return name;
        }
        return FSUtils.loadFromClasspath(name);
    }

    public static String loadFromClasspath(String name)
    {
        URL url = Thread.currentThread().getContextClassLoader().getResource(name);
        if(url == null)
        {
            return null;
        }
        try
        {
            //for urls with white spaces we need to convert to URI 
            URI uri = normalize(url.toURI());
            File file = new File(uri);
            String path = file.getAbsolutePath();
            return path;
        }
        catch (URISyntaxException e)
        {
            throw new NotImplementedYet(e);
        }
    }

    private static URI normalize(URI uri)
        throws URISyntaxException
    {
        String path = uri.toString();
        //the following prevents 'URI is not hierarchical' exception thrown by File(URI)
        if(path.startsWith("jar:"))
        {
            path = path.substring("jar:".length());
            uri = new URI(path);
        }
        return uri;
    }
    
    public static InputStream loadStream(String name)
    {
    	InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
    	return is;
    }

    public static InputStream toInputStream(String name)
        throws Exception
    {
        String file = FSUtils.load(name);
        return new FileInputStream(file);
    }

    public static String readFile(String name)
        throws Exception
    {
        File file = new File(FSUtils.load(name));
        return FileUtils.readFileToString(file);
    }
}
