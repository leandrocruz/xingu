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

	private Map<String, Object>	nodeById	= new HashMap<String, Object>();
	
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
			push(node, attrs);
		}
		else if("ref".equals(name))
		{
			Object node = nodeById.get(id);
			push(node, attrs);
		}
		else
		{
			throw new NotImplementedYet();
		}
	}

	private void push(Object node, Attributes attrs)
	{
		String id = attrs.getValue("id");
		nodeById.put(id, node);
		stack.push(new Node(node, attrs));
	}

	private Object handleNode(Attributes attrs)
		throws SAXException
	{
		try
		{
			return nodeFrom(attrs);
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			throw new SAXException("Error creating node", e);
		}
	}

	private Object nodeFrom(Attributes attrs)
		throws Exception
	{
		String      className = attrs.getValue("class");
		String      value     = attrs.getValue("value");
		TypeHandler handler   = registry.get(className);
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
		Node node = stack.pop();
		if(stack.empty())
		{
			result = node.obj;
			return;
		}
		
		if("node".equals(name) || "ref".equals(name))
		{
			Node target = stack.peek();
			try
			{
				attach(target, node);
			}
			catch(Exception e)
			{
				//e.printStackTrace();
				throw new SAXException(e);
			}
		}
	}

	private void attach(Node target, Node value)
		throws Exception
	{
		String targetName = target.obj.getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(target.obj));;
		String field = value.field;
		if(field != null)
		{
			//System.err.println(targetName + "." + field + " <- " + value.obj);
			FieldUtils.setField(target.obj, field, value.obj);
			return;
		}

		//System.err.println(targetName + " + " + value.obj);
		Class<? extends Object> clazz = target.obj.getClass();
		Type type = ObjectType.typeFor(clazz);
		switch(type)
		{
			case ARRAY:
				int len = Array.getLength(target.obj);
				target.obj = ArrayUtils.resizeArray(target.obj, len + 1);
				Array.set(target.obj, len, value.obj);
				return;

			case COLLECTION:
				Collection<Object> coll = (Collection<Object>) target.obj;
				coll.add(value.obj);
				return;
				
			default:
				throw new NotImplementedYet();
		}
	}
}

class Node
{
	Object	obj;

	String	id;

	String	field;

	String	className;

	String	value;

	public Node(Object obj, Attributes attrs)
	{
		this.obj       = obj;
		this.id        = attrs.getValue("id");
		this.className = attrs.getValue("class");
		this.field     = attrs.getValue("field");
		this.value     = attrs.getValue("value");
	}

	@Override
	public String toString()
	{
		return "node: " + id + "/" + className;
	}
	
	
}
