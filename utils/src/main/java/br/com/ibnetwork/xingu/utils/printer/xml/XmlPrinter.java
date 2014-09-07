package br.com.ibnetwork.xingu.utils.printer.xml;

import org.apache.commons.lang3.StringUtils;

public class XmlPrinter
{
	private int				depth	= 0;

	private StringBuffer	sb		= new StringBuffer();

	private String			ident	= "\t";	
	
	public XmlPrinter(String ident)
	{
		this.ident = ident;
	}

	public XmlPrinter increment()
	{
		depth++;
		return this;
	}

	public XmlPrinter decrement()
	{
		depth--;
		return this;
	}
	
	public XmlPrinter br()
	{
		sb.append("\n");
		return this;
	}
	

	public XmlPrinter startElement(String name)
	{
		sb.append("<").append(name);
		return this;
	}

	public XmlPrinter endElement(String name)
	{
		sb.append("</").append(name).append(">");
		return this;
	}

	public XmlPrinter value(Object value)
	{
		sb.append(value);
		return this;
	}

	public XmlPrinter close()
	{
		sb.append(">");
		return this;
	}
	
	public XmlPrinter closeEmpty()
	{
		sb.append("/>");
		return this;
	}

	public XmlPrinter attr(String name, int value)
	{
		attr(name, String.valueOf(value));
		return this;
	}

	public XmlPrinter attrIf(String name, String value)
	{
		if(value != null)
		{
			attr(name, value);
		}
		return this;
	}

	public XmlPrinter attr(String name, String value)
	{
		sb.append(" ").append(name).append("=\"").append(value).append("\"");
		return this;
	}

	public XmlPrinter ident()
	{
		sb.append(StringUtils.repeat(ident, depth));
		return this;
	}

	@Override
	public String toString()
	{
		return sb.toString();
	}
}