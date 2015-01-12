package xingu.utils.io;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.filefilter.TrueFileFilter;

public class TreeVisitor
{
	private final File root;
	
	private final FileVisitor visitor;

	public TreeVisitor(File root, FileVisitor visitor)
	{
		this.root = root;
		this.visitor = visitor;
	}

	public void visit()
		throws Exception
	{
		visitRecursively(root);
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