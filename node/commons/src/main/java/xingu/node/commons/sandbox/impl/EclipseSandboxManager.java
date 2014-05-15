package xingu.node.commons.sandbox.impl;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import xingu.node.commons.sandbox.Sandbox;
import br.com.ibnetwork.xingu.utils.classloader.NamedClassLoader;
import br.com.ibnetwork.xingu.utils.classloader.eclipse.EclipseClassLoader;
import br.com.ibnetwork.xingu.utils.classloader.eclipse.Project;
import br.com.ibnetwork.xingu.utils.classloader.eclipse.Workspace;

public class EclipseSandboxManager
	extends SandboxManagerImpl
{
	private Workspace			workspace;

	private String				workspaceDirectory;

	private Map<String, String>	classpathVariables	= new HashMap<String, String>();

	@Override
	public void configure(Configuration conf)
		throws ConfigurationException
	{
		super.configure(conf);
		workspaceDirectory = conf.getChild("workspace").getAttribute("dir");
		Configuration[] variables = conf.getChild("workspace").getChildren("var");
		for(Configuration var : variables)
		{
			String name  = var.getAttribute("name");
			String value = var.getAttribute("value");
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
	protected File[] getSandboxFiles()
	{
		return local.listFiles((FileFilter) new SuffixFileFilter(".xml"));
	}

	@Override
	protected File sourceDirectoryFor(String id)
		throws Exception
	{
		File file = new File(local, id + ".xml");
		Configuration conf = new DefaultConfigurationBuilder().buildFromFile(file);
		String src = conf.getAttribute("src");
		return new File(src);
	}

	@Override
	protected NamedClassLoader buildClassLoader(String id, File src, Sandbox parentSandbox)
		throws Exception
	{
		Project project = workspace.byPath("/oystr-" + id);
		EclipseClassLoader factory = new EclipseClassLoader(workspace, project.getRoot());
		return factory.buildClassLoader(id, parentSandbox.classLoader());
	}
}