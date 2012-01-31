package br.com.ibnetwork.xingu.attributes.impl;

import br.com.ibnetwork.xingu.attributes.AttributeType;

public class AttributeTypeImpl
	implements AttributeType
{

    private String name;
	
	private Class javaType = Object.class;

	public AttributeTypeImpl(String name)
	{
		this.name = name;
	}

	public AttributeTypeImpl(String type,Class clazz)
	{
		this.name = type.toUpperCase();
		this.javaType = clazz;
	}

	public Class getJavaType()
	{
		return javaType;
	}

	public String getTypeName()
	{
		return name;
	}

	public boolean equals(Object other)
	{
		if(this == other) return true;
		if(other instanceof AttributeType)
		{
			AttributeType obj = (AttributeType) other;
			boolean result = obj.getJavaType().equals(javaType) && obj.getTypeName().equals(name);
			return result;
		}
		return false;
	}

	public String toString()
	{
		return "AttributeType: type("+name+") java type ("+javaType+")";
	}
}
