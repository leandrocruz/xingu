package br.com.ibnetwork.xingu.utils.type.impl;

import java.util.Date;

import br.com.ibnetwork.xingu.utils.type.ObjectType.Type;

public class DateTypeHandler
	extends TypeHandlerSupport
{
	public DateTypeHandler()
	{
		super(Date.class, "date", Type.PRIMITIVE);
	}

	@Override
	public String toString(Object obj)
	{
		Date date = Date.class.cast(obj);
		return String.valueOf(date.getTime());
	}

	@Override
	public Object toObject(String value)
	{
		long time = Long.valueOf(value);
		return new Date(time);
	}
}
