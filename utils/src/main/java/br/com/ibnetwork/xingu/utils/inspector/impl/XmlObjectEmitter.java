package br.com.ibnetwork.xingu.utils.inspector.impl;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.ArrayUtils;
import br.com.ibnetwork.xingu.utils.FieldUtils;
import br.com.ibnetwork.xingu.utils.inspector.ObjectEmitter;
import br.com.ibnetwork.xingu.utils.type.ObjectType;
import br.com.ibnetwork.xingu.utils.type.ObjectType.Type;
import br.com.ibnetwork.xingu.utils.type.TypeHandler;
import br.com.ibnetwork.xingu.utils.type.TypeHandlerRegistry;

public class XmlObjectEmitter
	extends DefaultHandler
	implements ObjectEmitter
{
	private TypeHandlerRegistry	registry;

	private Stack<Object>		stack		= new Stack<Object>();

	private Map<String, Object>	nodeById	= new HashMap<String, Object>();

	private Object				result;
	
	public XmlObjectEmitter(TypeHandlerRegistry aliases, ClassLoader unused)
	{
		this.registry = aliases;
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
			nodeById.put(id, node);
			stack.push(node);
		}
		else if("ref".equals(name))
		{
			Object node = nodeById.get(id);
			try
			{
				attachToPeek(node, attrs);
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
			attachToPeek(node, attrs);

			return node;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new SAXException("Error creating node", e);
		}
	}

	private void attachToPeek(Object node, Attributes attrs)
		throws Exception
	{
		int size = stack.size();
		if(size == 0)
		{
			return;
		}

		Object parent = stack.peek();
		String field  = attrs.getValue("field");
		if(field != null)
		{
			FieldUtils.setField(parent, field, node);
			return;
		}
		
		Class<? extends Object> clazz = parent.getClass();
		Type type = ObjectType.typeFor(clazz);
		switch(type)
		{
			case ARRAY:
				int len = Array.getLength(parent);
				Object enlarged = ArrayUtils.resizeArray(parent, len + 1);
				Array.set(enlarged, len, node);
				stack.set(size - 1, enlarged);
				break;

			case COLLECTION:
				Collection<Object> coll = (Collection<Object>) parent;
				coll.add(node);
				break;
				
			default:
				throw new NotImplementedYet();
		}
	}

	private Object nodeFrom(Attributes attrs)
		throws Exception
	{
		String      id        = attrs.getValue("id");
		String      className = attrs.getValue("class");
		String      value     = attrs.getValue("value");
		TypeHandler handler   = registry.get(className);

		System.out.println("node: " + id + "/" + className);

		if(handler != null && StringUtils.isNotEmpty(value))
		{
			return handler.toObject(value);
		}
		return handler.newInstance();
	}

	@Override
	public void endElement(String namespaceURI, String localName, String name)
		throws SAXException
	{
		if("node".equals(name))
		{
			result = stack.pop();
		}
	}
}
