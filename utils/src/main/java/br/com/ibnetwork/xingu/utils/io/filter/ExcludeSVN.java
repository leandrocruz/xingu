package br.com.ibnetwork.xingu.utils.io.filter;

import java.io.File;
import java.io.FileFilter;

public class ExcludeSVN
	implements FileFilter
{

	@Override
	public boolean accept(File file)
	{
		String name = file.getName(); 
		return !".svn".equals(name);
	}

}
