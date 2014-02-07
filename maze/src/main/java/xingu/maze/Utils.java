package xingu.maze;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import xingu.maze.impl.ConfigParserImpl;

public class Utils
{
    public static String configurationFileName()
    {
        String fileName = System.getProperty("maze.conf");
        if(fileName == null)
        {
            URL url = contextClassLoader().getResource("maze.conf");
            fileName = url.getFile();
        }
        return fileName;
    }

    public static Config config(String resourceName)
        throws Exception
    {
        File file = new File(resourceName);
        System.out.println("Parsing configuration from: "+file.getAbsolutePath());
        ConfigParser parser = new ConfigParserImpl(new FileInputStream(file));
        Config config = parser.parse();
        return config;
    }

    private static ClassLoader contextClassLoader()
    {
        return Thread.currentThread().getContextClassLoader();
    }

    public static final boolean isEmpty(String s)
    {
        return (s == null || s.length() == 0 || s.trim().length() == 0);
    }

    public static List<String> readLines(InputStream is) 
        throws IOException
    {
        return readLines(is, true, new String[]{"#","//"});
    }
    
    /**
     * Reads all lines from an InputStream ignoring lines that start with commenChar if ignoreComments is true
     */
    public static List<String> readLines(InputStream is, boolean ignoreComments, String... commentChars) 
        throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = null;
        List<String> result = new ArrayList<String>();
        while((line = reader.readLine()) != null)
        {
            //ignore empty lines
            if(line.trim().equals(""))
            {
                continue;
            }
            
            //test comments
            boolean ignoreLine = false;
            if(ignoreComments)
            {
                for(String commentChar : commentChars)
                {
                    if(line.startsWith(commentChar))
                    {
                        ignoreLine = true;
                        break;
                    }
                }
            }
            
            if(!ignoreLine)
            {
                result.add(line);
            }
        }
        return result;
    }

    public static Method getMain(Class<?> clazz) 
        throws NoSuchMethodException
    {
        Method[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; ++i)
        {
            if (!"main".equals(methods[i].getName()))
            {
                continue;
            }

            int modifiers = methods[i].getModifiers();
            if (!(Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)))
            {
                continue;
            }

            if (methods[i].getReturnType() != Void.TYPE)
            {
                continue;
            }

            Class<?>[] paramTypes = methods[i].getParameterTypes();

            if (paramTypes.length != 1)
            {
                continue;
            }

            if (paramTypes[0] != String[].class)
            {
                continue;
            }

            return methods[i];
        }
        throw new NoSuchMethodException("public static void main(String[] args) in " + clazz.getName());
    }
}
