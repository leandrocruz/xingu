package br.com.ibnetwork.xingu.utils.inspector.impl;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;

import br.com.ibnetwork.xingu.utils.classloader.ClassLoaderUtils;
import br.com.ibnetwork.xingu.utils.inspector.ObjectVisitor;
import br.com.ibnetwork.xingu.utils.inspector.ObjectType.Type;

public class XmlEmitter
	implements ObjectVisitor<String>
{
	private int          depth = 0;

	private StringBuffer sb    = new StringBuffer();
	
	@Override
	public void nodeStart(Object obj, Field field, Type type)
	{
		Class<?> clazz 			= obj.getClass();
		String  className       = clazz.getName();
		String  classLoaderName = ClassLoaderUtils.nameFor(clazz);
		int     id              = System.identityHashCode(obj);
		sb
			.append(ident())
			.append("<node")
			.append(" id=\"").append(id).append("\"");

		if(field != null)
		{
			sb.append(" attr=\"").append(field.getName()).append("\"");
		}
		
		sb.append(" class=\"").append(className).append("\"");
		
		if(classLoaderName != null)
		{
			sb.append(" classLoader=\"").append("TODO").append("\"");
		}

		sb.append(">").append("\n");
		depth++;
	}

	@Override
	public void nodeEmpty(Field field)
	{
		Class<?> clazz           = field.getType();
		String   className       = clazz.getName();
		String   classLoaderName = ClassLoaderUtils.nameFor(clazz);
		
		sb
			.append(ident())
			.append("<node")
			.append(" attr=\"").append(field.getName()).append("\"")
			.append(" class=\"").append(className).append("\"");
		if(classLoaderName != null)
		{
			sb.append(" classLoader=\"").append(classLoaderName).append("\"");
			
		}
		
		sb.append("/>\n");
	}

	@Override
	public void nodeEnd(Object obj, Field field, Type type)
	{
		depth--;
		sb
			.append(ident())
			.append("</node>")
			.append("\n");
	}

	@Override
	public void field(Object obj, Field field, Object value)
	{
		String name = field.getName();
		sb
			.append(ident())
			.append("<").append(name).append(">")
			.append(value)
			.append("</").append(name).append(">")
			.append("\n");
	}

	private String ident()
	{
		return StringUtils.repeat("\t", depth);
	}
	
	@Override
	public String getResult()
	{
		return sb.toString();
	}
}
