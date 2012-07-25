package br.com.ibnetwork.xingu.utils.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.filefilter.RegexFileFilter;

import br.com.ibnetwork.xingu.utils.StringUtils;

public class FileUtils
{
	
	public static final DateFormat df = new SimpleDateFormat("yyyyMMdd.HHmmss");
	
	public static final String COMMENT_LINE = "#"; 

	public static Map<String, String> toMap(InputStream is)
		throws IOException
	{
		return toMap(is, "=");
	}
	
	public static Map<String, String> toMap(InputStream is, String separator)
		throws IOException
	{
		Map<String, String> result = new HashMap<String, String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while((line = reader.readLine()) != null)
		{
			boolean bypass = line.startsWith(COMMENT_LINE);
			if(bypass)
			{
				continue;
			}
			
			int idx = line.indexOf(separator);
			if(idx < 0)
			{
				continue;
			}
			
			String key = trim(line.substring(0, idx)); 
			String value = trim(line.substring(idx + 1));
			result.put(key, value);
		}
		return result;
	}
	
    private static String trim(String s)
	{
    	return StringUtils.trimToNull(s);
	}

	public static String findRealName(String name)
    {
        int idx = name.indexOf("*");
        if(idx < 0 )
        {
            return name;
        }
        File file = new File(name);
        File root = file.getParentFile();
        int start = root.getAbsolutePath().length();
        String prefix = file.getAbsolutePath().substring(start + 1, idx);
        String regexp = "^"+prefix+"(.+)$";
        FileFilter fileFilter = new RegexFileFilter(regexp);
        File[] children = root.listFiles(fileFilter);
        if(children == null || children.length != 1)
        {
            return name;
        }
        return children[0].getAbsolutePath();
    }
    
	public static File createTempDir()
			throws IOException
	{
		return createTempDir(null);
	}
	
	public static File createTempDir(String prefix)
		throws IOException
	{
		final File temp = new File(System.getProperty("java.io.tmpdir"));
		File dir;
		final int maxAttempts = 9;
		int attemptCount = 0;
		do
		{
			attemptCount++;
			if(attemptCount > maxAttempts)
			{
				throw new IOException("The highly improbable has occurred! Failed to create a unique temporary directory after " + maxAttempts + " attempts.");
			}
			String rnd = df.format(new Date());
			String dirName = prefix == null ? rnd : prefix + rnd; 
			dir = new File(temp, dirName);
		}
		while(dir.exists());

		if(dir.mkdirs())
		{
			return dir;
		}
		else
		{
			throw new IOException("Failed to create temp dir named " + dir.getAbsolutePath());
		}
	}
}
