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
import br.com.ibnetwork.xingu.utils.classloader.ClassLoaderManager;
import br.com.ibnetwork.xingu.utils.classloader.NamedClassLoader;
import br.com.ibnetwork.xingu.utils.inspector.ObjectEmitter;
import br.com.ibnetwork.xingu.utils.type.ObjectType;
import br.com.ibnetwork.xingu.utils.type.ObjectType.Type;
import br.com.ibnetwork.xingu.utils.type.TypeHandler;
import br.com.ibnetwork.xingu.utils.type.TypeHandlerRegistry;

public class XmlReader
	extends DefaultHandler
	implements ObjectEmitter
{
	private ClassLoaderManager	clm;

	private TypeHandlerRegistry	registry;

	private Object				result;

	private Stack<Node>			stack		= new Stack<Node>();

	private Map<String, Node>	nodeById	= new HashMap<String, Node>();
	
	public XmlReader(TypeHandlerRegistry registry, ClassLoaderManager clm)
	{
		this.clm      = clm;
		this.registry = registry;
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
		Node node = new Node(name, attrs);
		if("ref".equals(name))
		{
			node.payload = nodeById.get(node.id).payload;
		}
		else
		{
			nodeById.put(node.id, node);
			node.payload = getPayload(node);
		}
		return node;
	}

	private Object getPayload(Node node)
		throws Exception
	{
		String      type    = node.clazz != null ? node.clazz : node.name;
		TypeHandler handler = registry.get(type);
		if(handler != null && StringUtils.isNotEmpty(node.value))
		{
			return handler.toObject(node.value);
		}
		
		if(handler == null)
		{
			NamedClassLoader cl    = clm.byId(node.classLoader);
			Class<?>          clazz = cl.loadClass(node.clazz);
			handler                 = registry.handlerFor(clazz, Type.OBJECT);
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

class MapEntry
{
	public Object	key;

	public Object	value;

	public MapEntry(Object key)
	{
		this.key = key;
	}

	public void set(Object value)
	{
		this.value = value;
	}
}

class Node
{
	String	name;

	String	id;

	String	type;

	String	field;

	String	clazz;

	String	classLoader;

	String	value;

	Object	payload;
	
	MapEntry entry;

	public Node(String name, Attributes attrs)
	{
		this.name        = name;
		this.id          = attrs.getValue("id");
		this.clazz       = attrs.getValue("class");
		this.classLoader = attrs.getValue("classLoader");
		this.field       = attrs.getValue("field");
		this.value       = attrs.getValue("value");
		this.type        = attrs.getValue("type");
	}
	
	public void attach(Node value)
		throws Exception
	{
		if(value.field != null)
		{
			FieldUtils.setField(payload, value.field, value.payload);
		}
		else
		{
			Class<?> clazz = payload.getClass();
			Type type	   = ObjectType.typeFor(clazz);
			switch(type)
			{
				case ARRAY:
					
					int len = Array.getLength(payload);
					payload = ArrayUtils.resizeArray(payload, len + 1);
					Array.set(payload, len, value.payload);
					
					return;
					
				case COLLECTION:
					
					@SuppressWarnings("unchecked")
					Collection<Object> coll = (Collection<Object>) payload;
					coll.add(value.payload);
					
					return;
				
				case MAP:
					
					if(entry == null)
					{
						entry = new MapEntry(value.payload);
						return;
					}
					else
					{
						entry.set(value.payload);
					}
					
					Map map = (Map) payload;
					map.put(entry.key, entry.value);
					entry = null;
					
					return;
					
				default:
					throw new NotImplementedYet();
			}
		}

	}

	@Override
	public String toString()
	{
		return "name:"+name+",id:"+id+", type:"+type+", field:"+field+", class:"+clazz+", value:"+value;
	}
}
