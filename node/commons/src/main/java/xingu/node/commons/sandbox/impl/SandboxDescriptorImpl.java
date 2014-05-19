package xingu.node.commons.sandbox.impl;

import java.io.File;

import xingu.node.commons.sandbox.SandboxDescriptor;

public class SandboxDescriptorImpl
	implements SandboxDescriptor
{
	private String	id;

	private File	file;

	public SandboxDescriptorImpl(String id, File file)
	{
		this.id   = id;
		this.file = file;
	}
	
	@Override
	public String getId()
	{
		return id;
	}

	@Override
	public File getFile()
	{
		return file;
	}

	@Override
	public String toString()
	{
		return "id#" + id + " file#" + file;
	}
}
