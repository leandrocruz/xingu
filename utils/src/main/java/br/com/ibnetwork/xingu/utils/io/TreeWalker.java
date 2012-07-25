package br.com.ibnetwork.xingu.utils.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.ibnetwork.xingu.utils.io.filter.DirectoryOnly;

public class TreeWalker
{
	private final File root;
	
	private List<File> result = new ArrayList<File>();

	private final String wanted;

	public TreeWalker(File root, String wanted)
	{
		this.root = root;
		this.wanted = wanted;
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
			if(wanted.equals(file.getName()))
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