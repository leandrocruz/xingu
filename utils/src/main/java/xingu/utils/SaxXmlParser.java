package xingu.utils;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxXmlParser
{
	public static void parse(String input, DefaultHandler handler)
		throws SAXException, IOException, ParserConfigurationException
	{
		InputStream is = IOUtils.toInputStream(input);
		parse(is, handler);
	}

	public static void parse(InputStream is, DefaultHandler handler)
		throws SAXException, IOException, ParserConfigurationException
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser        parser  = factory.newSAXParser();
		parser.parse(is, handler);
	}
}