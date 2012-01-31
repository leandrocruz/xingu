package br.com.ibnetwork.xingu.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class Download
{
	public static File getWithMD5(String location)
		throws Exception
	{
		//MD5
		int idx = location.lastIndexOf(".");
		String md5Location = location.substring(0, idx) + ".md5";
		File md5File = get(md5Location);
		InputStream is = new FileInputStream(md5File);
		String expected = IOUtils.toString(is).trim();
		IOUtils.closeQuietly(is);
		
		//The File
		File file = get(location);
		is = new FileInputStream(file);
		byte[] data = IOUtils.toByteArray(is);
		IOUtils.closeQuietly(is);
		String md5 = MD5Utils.md5Hash(data);
		
		if(!expected.equals(md5))
		{
			throw new NotImplementedYet("MD5 mismatch. Got " + md5 + ", expected was " + expected);
		}
		
		return file;
	}

	public static File get(String location)
		throws Exception
	{
		URL url = new URL(location);
		InputStream is = (InputStream) url.getContent();
		File file = File.createTempFile("download-", ".tmp");
		byte[] data = IOUtils.toByteArray(is);
		IOUtils.closeQuietly(is);
		OutputStream os = new FileOutputStream(file);
		IOUtils.write(data, os);
		IOUtils.closeQuietly(os);
		return file;
	}

}
