package br.com.ibnetwork.xingu.utils.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.ibnetwork.xingu.utils.io.filter.DirectoryOnly;

public class TreeWalker
{
	private final String name = "build.d";
	
	private final File root;
	
	private List<File> result = new ArrayList<File>();

	public TreeWalker(File root)
	{
		this.root = root;
	}

	public List<File> walk()
	{
		return walkRecursively(root);
	}

	private List<File> walkRecursively(File start)
	{
		File[] files = start.listFiles(new DirectoryOnly());
		for (File file : files)
		{
			if(name.equals(file.getName()))
			{
				result.add(file);
			}
			else
			{
				walkRecursively(file);
			}
		}
		return result;
	}

}