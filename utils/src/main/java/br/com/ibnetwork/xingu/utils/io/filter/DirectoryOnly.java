package br.com.ibnetwork.xingu.utils.io.filter;

import java.io.File;
import java.io.FileFilter;

public class DirectoryOnly
	implements FileFilter
{
	@Override
	public boolean accept(File file)
	{
		String name = file.getName(); 
		return file.isDirectory() && !".svn".equals(name);
	}
}
