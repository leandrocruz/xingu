package xingu.utils.classloader.eclipse;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xingu.lang.NotImplementedYet;
import xingu.utils.io.FileUtils;

public class Workspace
{
	private static final String		M2_REPO				= "M2_REPO";

	private Pattern					pattern				= Pattern.compile("<projectDescription>\\s+<name>(.+)</name>");

	private List<Project>			projects;

	private Map<String, Project>	byPath				= new HashMap<String, Project>();

	private Map<String, String>		classPathVariables	= new HashMap<String, String>();

	public Workspace(File root)
		throws Exception
	{
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

	public URL resolve(String path)
		throws MalformedURLException
	{
		if(path.startsWith(M2_REPO))
		{
			path = path.substring(M2_REPO.length() + 1);
			path = classPathVariables.get(M2_REPO) + File.separator + path;
		}
		return new File(path).toURI().toURL();
	}

	public void addClasspathVariable(String name, String value)
	{
		classPathVariables.put(name, value);
	}
}
