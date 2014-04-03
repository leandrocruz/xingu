package br.com.ibnetwork.xingu.utils.inspector.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.FieldUtils;
import br.com.ibnetwork.xingu.utils.inspector.ObjectEmitter;
import br.com.ibnetwork.xingu.utils.inspector.TypeHandler;
import br.com.ibnetwork.xingu.utils.inspector.TypeHandlerRegistry;

public class XmlObjectEmitter
	extends DefaultHandler
	implements ObjectEmitter
{
	private TypeHandlerRegistry	registry;

	private ClassLoader			cl;

	private Stack<Object>		stack		= new Stack<Object>();

	private Map<String, Object>	nodeById	= new HashMap<String, Object>();

	private Objenesis			objenesis	= new ObjenesisStd();

	private Object				result;

	public XmlObjectEmitter(TypeHandlerRegistry aliases, ClassLoader cl)
	{
		this.registry = aliases;
		this.cl = cl;
	}

	@Override
	public Object from(String input)
		throws Exception
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
	    SAXParser        parser  = factory.newSAXParser();
	    InputStream      is      = IOUtils.toInputStream(input);
	    parser.parse(is, this);
		return result;
	}

	@Override
	public void startElement(String namespaceURI, String localName, String name, Attributes attrs)
		throws SAXException
	{
		String id = attrs.getValue("id");
		if("node".equals(name))
		{
			Object node = handleNode(attrs);
			if(result == null)
			{
				result = node;
			}
			nodeById.put(id, node);
			stack.push(node);
		}
		else if("ref".equals(name))
		{
			Object node = nodeById.get(id);
			String field = attrs.getValue("field");
			try
			{
				attachToPeek(node, field);
			}
			catch(Exception e)
			{
				throw new SAXException("Error handling object reference", e);
			}
		}
		else
		{
			throw new NotImplementedYet();
		}
	}

	private Object handleNode(Attributes attrs)
		throws SAXException
	{
		try
		{
			Object node  = nodeFrom(attrs);
			String field = attrs.getValue("field");
			if(StringUtils.isNotEmpty(field))
			{
				attachToPeek(node, field);
			}

			return node;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new SAXException("Error creating node", e);
		}
	}

	private void attachToPeek(Object node, String field)
		throws Exception
	{
		Object parent = stack.peek();
		FieldUtils.setField(parent, field, node);
	}

	private Object nodeFrom(Attributes attrs)
		throws Exception
	{
		String      id        = attrs.getValue("id");
		String      className = attrs.getValue("class");
		String      value     = attrs.getValue("value");
		//System.out.println("node: " + id + "/" + className);
		
		TypeHandler handler   = registry.get(className);
		if(handler != null && StringUtils.isNotEmpty(value))
		{
			return handler.toObject(value);
		}
		
		Class<?> clazz = handler.clazz();
		if(clazz == null)
		{
			clazz = cl.loadClass(className);
		}
		ObjectInstantiator<?> instantiatorOf = objenesis.getInstantiatorOf(clazz);
		return instantiatorOf.newInstance();
	}

	@Override
	public void endElement(String namespaceURI, String localName, String name)
		throws SAXException
	{
		if("node".equals(name))
		{
			stack.pop();
		}
	}
}
