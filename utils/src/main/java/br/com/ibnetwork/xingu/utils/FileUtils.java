package br.com.ibnetwork.xingu.utils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUtils
{
	public static File createTempDir()
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
			String dirName = UUID.randomUUID().toString();
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
