package xingu.codec.simple;

import java.text.DateFormat;
import java.util.Date;

import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;

import xingu.lang.NotImplementedYet;

public class DateTransformer
	implements Transform<Date>
{
	private DateFormat format;

	public DateTransformer(DateFormat format)
	{
		this.format = format;
	}

	@Override
	public Date read(String value)
		throws Exception
	{
		throw new NotImplementedYet();
	}

	@Override
	public String write(Date date)
		throws Exception
	{
		return format.format(date);
	}
}

