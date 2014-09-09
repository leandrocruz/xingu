package br.com.ibnetwork.xingu.utils.printer;

public interface Printer
{
	Printer increment();

	Printer decrement();

	Printer ident();

	Printer br();

	Printer startElement(String name);

	Printer endElement(String name);

	Printer value(Object value);

	Printer close();

	Printer closeEmpty();

	Printer attr(String name, int value);

	Printer attrIf(String name, String value);

	Printer attr(String name, String value);
}