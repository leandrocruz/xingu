package xingu.node.commons.sandbox.impl;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.commons.io.FilenameUtils;

import xingu.container.Environment;
import xingu.container.Inject;
import xingu.node.commons.sandbox.Sandbox;
import xingu.node.commons.sandbox.SandboxDescriptor;
import xingu.utils.classloader.NamedClassLoader;
import xingu.utils.classloader.eclipse.EclipseClassLoader;
import xingu.utils.classloader.eclipse.Project;
import xingu.utils.classloader.eclipse.Workspace;

public class EclipseSandboxManager
	extends SandboxManagerSupport
{
	@Inject
	private Environment			env;

	private Workspace			workspace;

	private String				workspaceDirectory;

	private File				local;

	private Map<String, String>	classpathVariables	= new HashMap<String, String>();

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		super.configure(conf);
		
		String dir = conf.getChild("repo").getAttribute("dir");
		this.local = new File(dir);
		
		workspaceDirectory = conf.getChild("workspace").getAttribute("dir");
		workspaceDirectory = env.replaceVars(workspaceDirectory);
		Configuration[] variables = conf.getChild("workspace").getChildren("var");
		for(Configuration var : variables)
		{
			String name  = var.getAttribute("name");
			String value = var.getAttribute("value");
			value = env.replaceVars(value);
			classpathVariables.put(name, value);
		}
	}
	
	@Override
	public void initialize()
		throws Exception
	{
		workspace = new Workspace(new File(workspaceDirectory));
		for(String name : classpathVariables.keySet())
		{
			String value = classpathVariables.get(name);
			workspace.addClasspathVariable(name, value);
		}
		super.initialize();
	}

	@Override
	protected NamedClassLoader buildClassLoader(String id, File src, Sandbox parentSandbox)
		throws Exception
	{
		Project project = workspace.byPath("/oystr-" + id);
		EclipseClassLoader factory = new EclipseClassLoader(workspace, project.getRoot());
		return factory.buildClassLoader(id, parentSandbox.classLoader());
	}

	@Override
	protected SandboxDescriptor[] getSandboxDescriptors()
		throws Exception
	{
		File[] files = local.listFiles(new FileFilter(){

			@Override
			public boolean accept(File file)
			{
				String  name     = file.getName();
				boolean isXml    = name.endsWith(".xml");
				boolean isBundle = "bundles.xml".equals(name);
				return isXml && !isBundle;
			}
		});
		
		SandboxDescriptor[] result = new SandboxDescriptor[files.length];
		
		int i = 0;
		for(File file : files)
		{
			String name = file.getName();
			String id = FilenameUtils.getBaseName(name);
			result[i++] = new SandboxDescriptorImpl(id, file);
		}
		return result;
	}

	@Override
	protected File sourceDirectoryFor(SandboxDescriptor desc)
		throws Exception
	{
		String id = desc.getId();
		File file = new File(local, id + ".xml");
		Configuration conf = new DefaultConfigurationBuilder().buildFromFile(file);
		String src = conf.getAttribute("src");
		return new File(src);
	}
}