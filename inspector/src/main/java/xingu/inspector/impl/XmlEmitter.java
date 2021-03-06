package xingu.inspector.impl;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringEscapeUtils;

import xingu.inspector.ObjectVisitor;
import xingu.lang.NotImplementedYet;
import xingu.type.ObjectType.Type;
import xingu.type.TypeHandler;
import xingu.utils.classloader.ClassLoaderUtils;
import xingu.utils.printer.Printer;
import xingu.utils.printer.xml.XmlPrinter;

public class XmlEmitter
	implements ObjectVisitor<String>
{
	private Printer printer = new XmlPrinter("\t");

	@Override
	public void onPrimitive(Object obj, String id, TypeHandler handler, Field field)
	{
		String fieldName = field == null ? null : field.getName();
		String value     = handler.toString(obj);
		String escaped	 = StringEscapeUtils.escapeXml(value);
		printer
			.ident()
			.startElement(handler.name())
			.attr("id", id)
			.attrIf("field", fieldName)
			.attr("value", escaped)
			.closeEmpty()
			.br();
	}

	private String typeName(TypeHandler handler)
	{
		Type type = handler.type();
		switch(type)
		{
			case ENUM:
			case ARRAY:
			case COLLECTION:
			case MAP:
				return type.name().toLowerCase();

			case OBJECT:
				return "obj";
				
			default:
				throw new NotImplementedYet();
		}
	}

	@Override
	public void onNodeStart(Object obj, String id, TypeHandler handler, Field field)
	{
		Class<?> clazz           = obj.getClass();
		String   className       = handler.name();
		String   classLoaderName = ClassLoaderUtils.nameFor(clazz);
		String   fieldName       = field == null ? null : field.getName();
		String   type            = typeName(handler);
		
		printer
			.ident()
			.startElement(type)
			.attr("id", id)
			.attrIf("field", fieldName)
			.attr("class", className)
			.attrIf("classLoader", classLoaderName)
			.close().br().increment();
	}

	@Override
	public void onNodeEnd(Object obj, String id, TypeHandler handler, Field field)
	{
		String type = typeName(handler);
		printer
			.decrement()
			.ident()
			.endElement(type)
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