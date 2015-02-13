package xingu.utils.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;

import xingu.lang.NotImplementedYet;
import xingu.utils.StringUtils;

public class FileUtils
{
	public static final DateFormat df = new SimpleDateFormat("yyyyMMdd.HHmmss");

	public static final String COMMENT_LINE = "#";

	public static InputStream toInputStream(String name)
		throws Exception
	{
		return toInputStream(name, Thread.currentThread().getContextClassLoader());
	}

	public static InputStream toInputStream(String name, ClassLoader cl)
		throws Exception
	{
		File file = new File(name);
        if(file.exists())
        {
        	return new FileInputStream(file);
        }
        else
        {
        	URL url = cl.getResource(name);
        	return url.openStream();
        }
	}

	public static byte[] toByteArray(File file)
		throws IOException
	{
		InputStream is     = new FileInputStream(file);
		byte[]      result = IOUtils.toByteArray(is);
		IOUtils.closeQuietly(is);
		return result;
	}

	public static String toString(File file)
		throws IOException
	{
		InputStream is     = new FileInputStream(file);
		String      result = IOUtils.toString(is);
		IOUtils.closeQuietly(is);
		return result;
	}
	
	public static void toFile(byte[] buffer, File file)
		throws IOException
	{
		OutputStream os = new FileOutputStream(file);
		IOUtils.write(buffer, os);
		IOUtils.closeQuietly(os);
	}

	public static void toFile(String s, File file)
		throws IOException
	{
		OutputStream os = new FileOutputStream(file);
		IOUtils.write(s, os);
		IOUtils.closeQuietly(os);
	}

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
		while ((line = reader.readLine()) != null)
		{
			boolean bypass = line.startsWith(COMMENT_LINE);
			if (bypass)
			{
				continue;
			}

			int idx = line.indexOf(separator);
			if (idx < 0)
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
		if (idx < 0)
		{
			return name;
		}
		File file = new File(name);
		File root = file.getParentFile();
		int start = root.getAbsolutePath().length();
		String prefix = file.getAbsolutePath().substring(start + 1, idx);
		String regexp = "^" + prefix + "(.+)$";
		FileFilter fileFilter = new RegexFileFilter(regexp);
		File[] children = root.listFiles(fileFilter);
		if (children == null || children.length != 1)
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
			if (attemptCount > maxAttempts)
			{
				throw new IOException("The highly improbable has occurred! Failed to create a unique temporary directory after " + maxAttempts + " attempts.");
			}
			String rnd = df.format(new Date());
			String dirName = prefix == null ? rnd : prefix + rnd;
			dir = new File(temp, dirName);
		}
		while (dir.exists());

		if (dir.mkdirs())
		{
			return dir;
		}
		else
		{
			throw new IOException("Failed to create temp dir named " + dir.getAbsolutePath());
		}
	}
	
	public static File toFile(InputStream is, String prefix, String suffix)
		throws IOException
	{
		File         file = File.createTempFile(prefix, suffix);
		OutputStream os   = new FileOutputStream(file);
		IOUtils.copy(is, os);
		IOUtils.closeQuietly(os);
		IOUtils.closeQuietly(is);
		
		return file;
	}

	public static File toFile(URL url, String prefix, String suffix)
		throws IOException
	{
		File         file = File.createTempFile(prefix, suffix);
		InputStream  is   = url.openStream();
		byte[]       data = IOUtils.toByteArray(is);
		OutputStream os   = new FileOutputStream(file);
		IOUtils.write(data, os);

		IOUtils.closeQuietly(os);
		IOUtils.closeQuietly(is);

		return file;
	}

	public static File createOrError(File parent, int name)
		throws IOException
	{
		return createOrError(parent, String.valueOf(name));
	}

	public static File createOrError(File parent, long name)
		throws IOException
	{
		return createOrError(parent, String.valueOf(name));
	}

	public static File createOrError(File parent, String name)
		throws IOException
	{
		File file = new File(parent, name);
		if(!file.exists())
		{
			boolean created = file.mkdirs();
			if(!created)
			{
				throw new NotImplementedYet("Can't create directory: " + file);
			}
		}
		return file;
	}

	public static File getTempDirectory()
	{
		String path = System.getProperty("java.io.tmpdir");
		return new File(path);
	}
}
