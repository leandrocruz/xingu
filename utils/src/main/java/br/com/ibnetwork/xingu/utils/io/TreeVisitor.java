package br.com.ibnetwork.xingu.utils.io;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.filefilter.TrueFileFilter;

public class TreeVisitor
{
	private final File root;
	
	private final FileVisitor visitor;

	private List<File> result = new ArrayList<File>();

	public TreeVisitor(File root, FileVisitor visitor)
	{
		this.root = root;
		this.visitor = visitor;
	}

	public List<File> visit()
		throws Exception
	{
		visitRecursively(root);
		return result;
	}

	private void visitRecursively(File start)
		throws Exception
	{
		File[] files = start.listFiles((FileFilter) TrueFileFilter.INSTANCE);
		for (File file : files)
		{
			visitor.visit(file);
			if(file.isDirectory())
			{
				visitRecursively(file);
			}
		}
	}

}