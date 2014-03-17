package br.com.ibnetwork.xingu.utils.classloader.eclipse;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import br.com.ibnetwork.xingu.utils.StringUtils;
import br.com.ibnetwork.xingu.utils.classloader.ClassLoaderFactory;

public class EclipseClassLoader
	implements ClassLoaderFactory
{
	private Workspace workspace;
	
	private File	project;
	
	public EclipseClassLoader(Workspace workspace, File project)
	{
		this.workspace = workspace;
		this.project   = project;
	}
	
	@Override
	public ClassLoader buildClassLoader(ClassLoader parent)
		throws Exception
	{
		Set<URL>       urls   = get(workspace, project);
	    URL[]          array  = urls.toArray(new URL[]{});
	    URLClassLoader result = new URLClassLoader(array, parent);
	    return result;
	}

	private Set<URL> get(Workspace workspace, File project)
		throws Exception
	{
		Set<URL>     urls         = new HashSet<URL>();
		SAXParser     master       = SAXParserFactory.newInstance().newSAXParser();
	    File          dotClasspath = new File(project, ".classpath");
		EclipseParser parser       = new EclipseParser();
	    master.parse(dotClasspath, parser);
	    
	    List<Entry> entries = parser.getEntries();
	    for(Entry entry : entries)
	    {
			String kind   = entry.kind;
			String path   = entry.path;
			//String output = entry.output;
			if("src".equals(kind))
			{
				if(path.startsWith(StringUtils.SLASH))
				{
					// a project
					Project  reference = workspace.byPath(path);
					Set<URL> set       = get(workspace, reference.root);
					urls.addAll(set);
				}
			}
			else if("var".equals(kind))
			{
				// a jar files
				File file = new File(path);
				urls.add(file.toURI().toURL());
			}
			else if("output".equals(kind))
			{
				// a source folder
				File file = new File(project, path + "/");
				urls.add(file.toURI().toURL());
			}
	    }
		return urls;
	}
}

class Entry
{
	String	kind;

	String	path;

	String	output;

	public Entry(String kind, String path, String output)
	{
		this.kind   = kind;
		this.path   = path;
		this.output = output;
	}
}
