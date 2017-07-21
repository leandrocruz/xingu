package xingu.utils.io;

import java.io.File;
import java.io.IOException;

import xingu.utils.MD5Utils;

public class TreeComparator
{
	private File	root1;

	private File	root2;

	public TreeComparator(File f1, File f2)
	{
		this.root1 = f1;
		this.root2 = f2;
	}

	public boolean compare()
		throws Exception
	{
		final Result result = new Result();
		
		TreeVisitor visitor = new TreeVisitor(root1, new FileVisitor()
		{
			@Override
			public void visit(File file)
				throws Exception
			{
				if(result.result)
				{
					String relative = relativeName(root1, file);
					File other = new File(root2, relative);
					result.result = compareFiles(file, other);
				}
			}
		});
		
		visitor.visit();
		return result.result;
	}

	private String relativeName(File dir, File file)
	{
		String path = dir.getAbsolutePath();
		String item = file.getAbsolutePath();
		if(!item.startsWith(path))
		{
			return null;
		}
		return item.substring(path.length());
	}

	private boolean compareFiles(File f1, File f2)
		throws IOException
	{
		//System.out.println("Comparing "+f1+" and " + f2);
		if(f1.isFile())
		{
			return compareMD5(f1, f2);
		}
		else
		{
			return compareDirectories(f1, f2);
		}
	}

	private boolean compareDirectories(File f1, File f2)
	{
		String n1 = f1.getName();
		String n2 = f2.getName();
		if(n1.equals(n2))
		{
			File[] l1 = f1.listFiles();
			File[] l2 = f2.listFiles();
			if(l1.length == l2.length)
			{
				for(int i = 0; i < l1.length; i++)
				{
					File i1 = l1[i];
					File i2 = l2[i];
					return i1.getName().equals(i2.getName());
				}
			}
		}
		return false;
	}

	private boolean compareMD5(File f1, File f2)
		throws IOException
	{
		String h1 = MD5Utils.md5Hash(f1);
		String h2 = MD5Utils.md5Hash(f2);
		return h1.equals(h2);
	}
}

class Result
{
	boolean result = true;
}
