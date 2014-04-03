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

	private Object				result;

	private Stack<Node>			stack		= new Stack<Node>();

	private Map<String, Node>	nodeById	= new HashMap<String, Node>();
	
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
		try
		{
			Node node = onStart(name, attrs);
			stack.push(node);
		}
		catch(Exception e)
		{
			throw new SAXException("Error creating node", e);
		}
	}

	private Node onStart(String name, Attributes attrs)
		throws Exception
	{
		Node node = new Node(attrs);
		if("node".equals(name))
		{
			nodeById.put(node.id, node);
			node.payload = getPayload(node);
		}
		else if("ref".equals(name))
		{
			node.payload = nodeById.get(node.id).payload;
		}
		else
		{
			throw new NotImplementedYet();
		}
		return node;
	}

	private Object getPayload(Node node)
		throws Exception
	{
		TypeHandler handler = registry.get(node.clazz);
		if(handler != null && StringUtils.isNotEmpty(node.value))
		{
			return handler.toObject(node.value);
		}
		return handler.newInstance();
	}

	@Override
	public void endElement(String namespaceURI, String localName, String name)
		throws SAXException
	{
		try
		{
			result = onEnd();
		}
		catch(Exception e)
		{
			throw new SAXException(e);
		}
	}

	public Object onEnd()
		throws Exception
	{
		Node value = stack.pop();
		if(stack.empty())
		{
			return value.payload;
		}
		
		Node target = stack.peek();
		target.attach(value);
		
		return null;
	}
}

class Node
{
	String	id;

	String	type;

	String	field;

	String	clazz;

	String	value;

	Object	payload;

	public Node(Attributes attrs)
	{
		this.id    = attrs.getValue("id");
		this.clazz = attrs.getValue("class");
		this.field = attrs.getValue("field");
		this.value = attrs.getValue("value");
		this.type  = attrs.getValue("type");
	}
	
	public void attach(Node value)
		throws Exception
	{
		if(value.field != null)
		{
			FieldUtils.setField(payload, value.field, value.payload);
			return;
		}

		Class<? extends Object> clazz = payload.getClass();
		Type type = ObjectType.typeFor(clazz);
		switch(type)
		{
			case ARRAY:
				int len = Array.getLength(payload);
				payload = ArrayUtils.resizeArray(payload, len + 1);
				Array.set(payload, len, value.payload);
				return;

			case COLLECTION:
				Collection<Object> coll = (Collection<Object>) payload;
				coll.add(value.payload);
				return;

			default:
				throw new NotImplementedYet();
		}
	}

	@Override
	public String toString()
	{
		return "id:"+id+", type:"+type+", field:"+field+", class:"+clazz+", value:"+value;
	}
}
