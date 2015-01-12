package xingu.utils.io.filter;

import java.io.File;
import java.io.FileFilter;

public class ZipFileFilter
	implements FileFilter
{
	@Override
	public boolean accept(File file)
	{
		String name = file.getName();
		return file.isDirectory() && !name.endsWith(".zip");
	}
}