package xingu.utils.classloader.eclipse;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class EclipseParser
	extends DefaultHandler
{
	private List<Entry> entries = new ArrayList<Entry>();
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
		throws SAXException
	{
		
		String kind   = attributes.getValue("kind");
		String path   = attributes.getValue("path");
		String output = attributes.getValue("output");
		entries.add(new Entry(kind, path, output));
	}

	public List<Entry> getEntries()
	{
		return entries;
	}
}
