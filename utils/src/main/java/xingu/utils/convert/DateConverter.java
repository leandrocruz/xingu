package xingu.utils.convert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.beanutils.Converter;

public class DateConverter
	implements Converter
{
	private Locale locale;
	
	private SimpleDateFormat defaultFormat;
	
	private String separator = "::";
	
	public DateConverter(Locale locale)
	{
		this.locale = locale;
		this.defaultFormat = new SimpleDateFormat("dd/MM/yyyy",locale);
	}
	
	public DateConverter(Locale locale, String separator)
	{
		this.locale = locale;
		this.defaultFormat = new SimpleDateFormat("dd/MM/yyyy",locale);
		this.separator = separator;
	}

	public Object convert(Class targetType, Object value)
	{
		//System.out.println("Type: "+type.getCanonicalName());
		//System.out.println("Value: "+value.getClass());
		if (value == null)
		{
			return null;
		}
		if(value instanceof String)
		{
			return fromStringToDate(value);
		}
		else if(value instanceof Date)
		{
			return fromDateToString(value);
		}
		return null;
	}

	private Object fromDateToString(Object value)
    {
		Date valueAsDate = (Date) value;
		SimpleDateFormat format = defaultFormat;
		return format.format(valueAsDate);
    }

	private Object fromStringToDate(Object value)
    {
		SimpleDateFormat format = defaultFormat;
		String valueAsString = (String) value;
		String mask = "";
		if (valueAsString.indexOf(separator) > -1)
		{
			String[] array = valueAsString.split(separator);
			if(array.length < 2)
			{
				return null;
			}
			mask = array[0];
			valueAsString = array[1];
			format = new SimpleDateFormat(mask,locale);
		}
		//System.out.println("converting type: "+type+" value:"+valueAsString+" format: "+mask);
		try
		{
			value = format.parse(valueAsString);
			return value;
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			return null;
		}
    }
}
