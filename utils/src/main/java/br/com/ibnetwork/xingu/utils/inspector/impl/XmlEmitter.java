package br.com.ibnetwork.xingu.utils.inspector.impl;

import java.lang.reflect.Field;

import br.com.ibnetwork.xingu.utils.classloader.ClassLoaderUtils;
import br.com.ibnetwork.xingu.utils.inspector.ObjectVisitor;
import br.com.ibnetwork.xingu.utils.inspector.TypeAlias;
import br.com.ibnetwork.xingu.utils.xml.XmlPrinter;

public class XmlEmitter
	implements ObjectVisitor<String>
{
	private XmlPrinter printer = new XmlPrinter("\t");
	
	@Override
	public void onNodeStart(Object obj, String id, TypeAlias alias, Field field)
	{
		
		Class<?> clazz           = obj.getClass();
		String   className       = alias.name();
		String   classLoaderName = ClassLoaderUtils.nameFor(clazz);
		String   fieldName       = field == null ? null : field.getName();
		
		printer
			.ident()
			.startElement("node")
			.attr("id", id)
			.attrIf("at", fieldName)
			.attr("type", alias.type().name())
			.attr("class", className)
			.attrIf("classLoader", classLoaderName)
			.close().br().increment();
	}

	@Override
	public void onNodeEnd(Object obj, String id, TypeAlias alias, Field field)
	{
		printer
			.decrement()
			.ident()
			.endElement("node")
			.br();
	}

	@Override
	public void onNodeReference(Object obj, String id, TypeAlias alias, Field field)
	{
		String fieldName = field == null ? null : field.getName();
		printer
			.ident()
			.startElement("ref")
			.attrIf("at", fieldName)
			.attr("id", id)
			.closeEmpty().br();
	}

	@Override
	public void onPrimitiveObjectField(Field field, Object value)
	{
		String name = field.getName();
		printer
			.ident()
			.startElement(name)
			.close()
			.value(value)
			.endElement(name)
			.br();
	}

	@Override
	public void onPrimitiveCollectionItem(Object value, String id, TypeAlias alias)
	{
		String name = alias.name();
		printer
			.ident()
			.startElement(name)
			.attr("id", id)
			.close()
			.value(value)
			.endElement(name)
			.br();
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

	/*
	@Override
	public void whenNodeEmpty(Field field)
	{
		Class<?> clazz           = field.getType();
		String   className       = clazz.getName();
		String   classLoaderName = ClassLoaderUtils.nameFor(clazz);
		
		printer
			.ident()
			.startElement("node")
			.attr("attr", field.getName())
			.attr("class", className)
			.attrIf("classLoader", classLoaderName)
			.closeEmpty().br();
	}
	*/
}