package xingu.node.commons.sandbox.impl;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.commons.io.FilenameUtils;

import xingu.node.commons.sandbox.Sandbox;
import xingu.node.commons.sandbox.SandboxDescriptor;
import br.com.ibnetwork.xingu.utils.classloader.NamedClassLoader;
import br.com.ibnetwork.xingu.utils.classloader.eclipse.EclipseClassLoader;
import br.com.ibnetwork.xingu.utils.classloader.eclipse.Project;
import br.com.ibnetwork.xingu.utils.classloader.eclipse.Workspace;

public class EclipseSandboxManager
	extends SandboxManagerSupport
{
	private Workspace			workspace;

	private String				workspaceDirectory;

	private Map<String, String>	classpathVariables	= new HashMap<String, String>();
	
	private File local;

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
				String name = file.getName();
				if(name.endsWith(".xml"))
				{
					return !"bundles.xml".equals(name);
				}
				return false;
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