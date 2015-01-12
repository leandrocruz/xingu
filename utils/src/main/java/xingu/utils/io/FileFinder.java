package xingu.utils.io;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileFinder
{
	private final File root;
	
	private List<File> result = new ArrayList<File>();

	private final FilenameFilter filter;

	public FileFinder(File root, FilenameFilter filter)
	{
		this.root = root;
		this.filter = filter;
	}

	public List<File> find()
		throws IOException
	{
		return findRecursively(root);
	}

	private List<File> findRecursively(File start)
		throws IOException
	{
		File[] files = start.listFiles();
		for (File file : files)
		{
			if(file.isDirectory())
			{
				findRecursively(file);
			}
			else
			{
				boolean add = filter.accept(file.getParentFile(), file.getName());
				if(add)
				{
					result.add(file.getCanonicalFile());
				}
			}
		}
		return result;
	}

}