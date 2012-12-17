package br.com.ibnetwork.xingu.utils.classloader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class TryChildClassLoader
	extends URLClassLoader
{
	private final ClassLoader myParent;
	
	private boolean tryParentFirst = false;

	public TryChildClassLoader(ClassLoader parent, boolean tryParentFirst)
		throws IOException
	{
		super(new URL[] {}, parent != null ? parent : (Thread.currentThread().getContextClassLoader() != null ? Thread.currentThread().getContextClassLoader() : (TryChildClassLoader.class.getClassLoader() != null ? TryChildClassLoader.class.getClassLoader() : ClassLoader.getSystemClassLoader())));
		myParent = getParent();
		if (myParent == null)
		{
			throw new IllegalArgumentException("no parent classloader!");
		}
		this.tryParentFirst = tryParentFirst;
	}

	private boolean isTryParentFirst()
	{
		return tryParentFirst;
	}

	public void add(File file)
		throws MalformedURLException
	{
		URL url = null;
		if(file.isDirectory())
		{
			url = new URL("file://" + file.getAbsolutePath() + "/");
		}
		else
		{
			url = new URL("file://" + file.getAbsolutePath());
		}
		addURL(url);
	}

	public URL getResource(String name)
	{
		URL url = null;
		boolean triedParent = false;
		boolean isParentLoaderPriority = isTryParentFirst();
		if (myParent != null && isParentLoaderPriority)
		{
			triedParent = true;
			url = myParent.getResource(name);
		}

		if (url == null)
		{
			url = this.findResource(name);
			if (url == null && name.startsWith("/"))
			{
				url = this.findResource(name.substring(1));
			}
		}

		if (url == null && !triedParent)
		{
			if (myParent != null)
			{
				url = myParent.getResource(name);
			}
		}

		return url;
	}

	@Override
	public Class<?> loadClass(String name)
		throws ClassNotFoundException
	{
		return loadClass(name, false);
	}

	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve)
		throws ClassNotFoundException
	{
		Class<?> clazz = findLoadedClass(name);
		ClassNotFoundException ex = null;
		
		boolean triedParent = false;
		boolean tryParentFirst = isTryParentFirst();

		if (tryParentFirst && myParent != null && clazz == null)
		{
			triedParent = true;
			try
			{
				clazz = myParent.loadClass(name);
			}
			catch (ClassNotFoundException e)
			{
				ex = e;
			}
		}

		if (clazz == null)
		{
			try
			{
				clazz = this.findClass(name);
			}
			catch (ClassNotFoundException e)
			{
				ex = e;
			}
		}

		if (clazz == null && myParent != null && !triedParent)
		{
			clazz = myParent.loadClass(name);
		}

		if (clazz == null)
		{
			throw ex;
		}

		if (resolve)
		{
			resolveClass(clazz);
		}

		return clazz;
	}
}
