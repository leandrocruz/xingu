package br.com.ibnetwork.xingu.utils.xml;

import static org.junit.Assert.*;

import org.junit.Test;

public class XmlPrinterTest
{
	@Test
	public void testNode()
		throws Exception
	{
		XmlPrinter printer = new XmlPrinter("\t");
		String result = printer
				.startElement("parent")
				.close()
				.endElement("parent")
				.toString();
		
		assertEquals("<parent></parent>", result);
	}

	@Test
	public void testNodeWithAttribute()
		throws Exception
	{
		XmlPrinter printer = new XmlPrinter("\t");
		String result = printer
				.startElement("parent")
				.attr("x", 1)
				.close()
				.endElement("parent")
				.toString();
		
		assertEquals("<parent x=\"1\"></parent>", result);
	}

	@Test
	public void testNodeWithAttributeAndValue()
		throws Exception
	{
		XmlPrinter printer = new XmlPrinter("\t");
		String result = printer
				.startElement("parent")
				.attr("x", 1)
				.close()
				.value("mmm")
				.endElement("parent")
				.toString();
		
		assertEquals("<parent x=\"1\">mmm</parent>", result);
	}

	@Test
	public void testNodeWithChild()
		throws Exception
	{
		XmlPrinter printer = new XmlPrinter("\t");
		String result = printer
				.startElement("parent")
				.attr("x", 1)
				.close()
				.startElement("child")
				.attr("y", 2)
				.close()
				.endElement("child")
				.endElement("parent")
				.toString();
		
		assertEquals("<parent x=\"1\"><child y=\"2\"></child></parent>", result);
	}

	@Test
	public void testNodeWithChildAndValueOnChild()
		throws Exception
	{
		XmlPrinter printer = new XmlPrinter("\t");
		String result = printer
				.startElement("parent")
				.attr("x", 1)
				.close()
				.startElement("child")
				.attr("y", 2)
				.close()
				.value("mmm")
				.endElement("child")
				.endElement("parent")
				.toString();
		
		assertEquals("<parent x=\"1\"><child y=\"2\">mmm</child></parent>", result);
	}

}
