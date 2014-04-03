package br.com.ibnetwork.xingu.utils.inspector.impl;

import java.lang.reflect.Field;

import br.com.ibnetwork.xingu.utils.classloader.ClassLoaderUtils;
import br.com.ibnetwork.xingu.utils.inspector.ObjectType.Type;
import br.com.ibnetwork.xingu.utils.inspector.ObjectVisitor;
import br.com.ibnetwork.xingu.utils.type.TypeHandler;
import br.com.ibnetwork.xingu.utils.xml.XmlPrinter;

public class XmlEmitter
	implements ObjectVisitor<String>
{
	private XmlPrinter printer = new XmlPrinter("\t");

	@Override
	public void onPrimitive(Object obj, String id, TypeHandler handler, Field field)
	{
		String fieldName = field == null ? null : field.getName();
		String type      = handler.type() == Type.ARRAY ? Type.ARRAY.name() : null;
		String value     = handler.toString(obj);
		printer
			.ident()
			.startElement("node")
			.attr("id", id)
			.attrIf("field", fieldName)
			.attrIf("type", type)
			.attr("class", handler.name())
			.attr("value", value)
			.closeEmpty()
			.br();
	}

	@Override
	public void onNodeStart(Object obj, String id, TypeHandler handler, Field field)
	{
		Class<?> clazz           = obj.getClass();
		String   className       = handler.name();
		String   classLoaderName = ClassLoaderUtils.nameFor(clazz);
		String   fieldName       = field == null ? null : field.getName();
		String   type            = handler.type() == Type.ARRAY ? Type.ARRAY.name() : null;
		
		printer
			.ident()
			.startElement("node")
			.attr("id", id)
			.attrIf("field", fieldName)
			.attrIf("type", type)
			.attr("class", className)
			.attrIf("classLoader", classLoaderName)
			.close().br().increment();
	}

	@Override
	public void onNodeEnd(Object obj, String id, TypeHandler handler, Field field)
	{
		printer
			.decrement()
			.ident()
			.endElement("node")
			.br();
	}

	@Override
	public void onNodeReference(Object obj, String id, TypeHandler handler, Field field)
	{
		String fieldName = field == null ? null : field.getName();
		printer
			.ident()
			.startElement("ref")
			.attr("id", id)
			.attrIf("field", fieldName)
			.closeEmpty().br();
	}

	@Override
	public String getResult()
	{
		return printer.toString();
	}

	@Override
	public String toString()
	{
		return printer.toString();
	}
}