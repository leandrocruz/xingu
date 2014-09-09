package br.com.ibnetwork.xingu.utils.printer.xml;

import br.com.ibnetwork.xingu.utils.printer.Printer;
import br.com.ibnetwork.xingu.utils.printer.PrinterSupport;

public class XmlPrinter
	extends PrinterSupport
	implements Printer
{
	public XmlPrinter(String ident)
	{
		super(ident);
	}

	@Override
	public Printer startElement(String name)
	{
		sb.append("<").append(name);
		return this;
	}

	@Override
	public Printer endElement(String name)
	{
		sb.append("</").append(name).append(">");
		return this;
	}

	@Override
	public Printer close()
	{
		sb.append(">");
		return this;
	}
	
	@Override
	public Printer closeEmpty()
	{
		sb.append("/>");
		return this;
	}

	@Override
	public Printer attr(String name, String value)
	{
		sb.append(" ").append(name).append("=\"").append(value).append("\"");
		return this;
	}
}