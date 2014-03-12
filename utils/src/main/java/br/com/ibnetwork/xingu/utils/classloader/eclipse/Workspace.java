package br.com.ibnetwork.xingu.utils.classloader.eclipse;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.io.FileUtils;

public class Workspace
{
	private Pattern					pattern	= Pattern.compile("<projectDescription>\\s+<name>(.+)</name>");

	private File					root;

	private List<Project>			projects;

	private Map<String, Project>	byPath	= new HashMap<String, Project>();

	public Workspace(File root)
		throws Exception
	{
		this.root     = root;
		this.projects = find(root);
	}
	
	private List<Project> find(File start)
		throws Exception
	{
		List<Project> result = new ArrayList<Project>();
		File[] children = start.listFiles();
		for(File file : children)
		{
			if(file.isDirectory())
			{
				List<Project> tmp = find(file);
				result.addAll(tmp);
			}
			else if(file.getName().equals(".project"))
			{
				//System.out.println(file);
				Project project = from(file);
				byPath.put("/" + project.name, project);
				result.add(project);
			}
		}
		return result;
	}

	private Project from(File file)
		throws Exception
	{
		String s = FileUtils.toString(file);
		Matcher m = pattern.matcher(s);
		if(m.find())
		{
			String name = m.group(1);
			return new Project(file.getParentFile(), name);
		}
		throw new NotImplementedYet("Can't parse name from '"+s+"'");
	}

	public Project byPath(String path)
	{
		return byPath.get(path);
	}
}
