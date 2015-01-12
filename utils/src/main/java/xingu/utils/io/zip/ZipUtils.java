package xingu.utils.io.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;

public class ZipUtils
{
	
	public static File gzip(File file)
		throws IOException
	{
		File to = new File(file.getParentFile(), file.getName() + ".gz");
		gzip(file, to);
		return to;
	}

	public static byte[] gzip(byte[] in)
		throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStream          os   = new BufferedOutputStream(new GZIPOutputStream(baos));
		InputStream           is   = new BufferedInputStream(new ByteArrayInputStream(in));
		IOUtils.copy(is, os);
		IOUtils.closeQuietly(is);
		IOUtils.closeQuietly(os);
		return baos.toByteArray();
	}
	
	public static void gzip(File file, File to)
		throws IOException
	{
		OutputStream os = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(to)));
		InputStream is = new BufferedInputStream(new FileInputStream(file));
		IOUtils.copy(is, os);
		IOUtils.closeQuietly(is);
		IOUtils.closeQuietly(os);
	}

	public static void explode(File file, File to)
		throws Exception
	{
		ZipFile zipFile = new ZipFile(file, ZipFile.OPEN_READ);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements())
		{
			ZipEntry entry = entries.nextElement();
			if(entry.isDirectory())
			{
				continue;
			}
			//System.out.println("Extracting: " + entry);
			File target = new File(to, entry.getName());
			File parent = target.getParentFile();
			if(!parent.exists())
			{
				parent.mkdirs();
			}
			BufferedInputStream is = new BufferedInputStream(zipFile.getInputStream(entry));
			BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(target));
			IOUtils.copy(is, os);
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}
		zipFile.close();
	}

	public static void zip(File fileOrDirectory, boolean includeRoot, File to)
		throws Exception
	{
		new ZipBuilder(fileOrDirectory, includeRoot).zipTo(to);
	}
}
