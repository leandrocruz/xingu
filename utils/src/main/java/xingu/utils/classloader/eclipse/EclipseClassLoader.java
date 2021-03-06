package xingu.utils.classloader.eclipse;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import xingu.utils.StringUtils;
import xingu.utils.classloader.ClassLoaderFactory;
import xingu.utils.classloader.NamedClassLoader;
import xingu.utils.classloader.impl.NamedClassLoaderImpl;

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
	public NamedClassLoader buildClassLoader(String name, NamedClassLoader parent)
		throws Exception
	{
		Set<URL>       urls   = get(workspace, project);
	    URL[]          array  = urls.toArray(new URL[]{});
	    return new NamedClassLoaderImpl(name, array, parent.getClassLoader());
	}

	private Set<URL> get(Workspace workspace, File project)
		throws Exception
	{
		Set<URL>      urls         = new HashSet<URL>();
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
			//System.out.println(kind + " " + path);
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
				// a jar file
				URL  url  = workspace.resolve(path);
				urls.add(url);
			}
			else if("output".equals(kind))
			{
				// a source folder
				URL  url  = new File(project, path + "/").toURI().toURL();
				urls.add(url);
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
