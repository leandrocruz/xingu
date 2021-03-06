package xingu.utils.classloader.eclipse;

import java.io.File;

public class Project
{
	File	root;
	
	String	name;

	public Project(File root, String name)
	{
		this.root = root;
		this.name = name;
	}

	@Override
	public String toString()
	{
		return name + " @" + root;
	}

	public File getRoot()
	{
		return root;
	}
}
