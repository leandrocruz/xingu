package br.com.ibnetwork.xingu.utils.inspector.impl;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;

import br.com.ibnetwork.xingu.utils.inspector.ObjectVisitor;
import br.com.ibnetwork.xingu.utils.inspector.ObjectType.Type;

public class XmlEmitter
	implements ObjectVisitor
{
	private int depth = 0;
	
	@Override
	public void nodeStart(Object obj, Field field, Type type)
	{
		String name  = "node";
		if(field != null)
		{
			name = field.getName();
		}
		String clazz = obj.getClass().getName();
		int    id    = System.identityHashCode(obj);
		print("<"+name+" class=\""+clazz+"\" id=\""+id+"\">");
		depth++;
	}

	@Override
	public void nodeEmpty(Field field)
	{
		print("<"+field.getName()+"/>");
	}

	@Override
	public void nodeEnd(Object obj, Field field, Type type)
	{
		depth--;
		String name  = "node";
		if(field != null)
		{
			name = field.getName();
		}
		print("</"+name+">" /* + obj.getClass().getName()*/);
	}

	@Override
	public void field(Object obj, Field field, Object value)
	{
		String name = field.getName();
		print("<"+name+">"+value+"</"+name+">");
	}

	private void print(String s)
	{
		String ident = StringUtils.repeat(" ", 2*depth);
		System.err.println(ident + s);
	}
}
