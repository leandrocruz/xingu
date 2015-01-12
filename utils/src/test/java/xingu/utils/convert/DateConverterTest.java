package xingu.utils.convert;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

import org.apache.commons.beanutils.ConvertUtils;

import xingu.utils.convert.DateConverter;

public class DateConverterTest
    extends TestCase
{
	static
	{
		ConvertUtils.register(new DateConverter(new Locale("pt","BR")), Date.class);
	}
	
	public void _testConvertToString()
		throws Exception
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 30);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.YEAR, 2000);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
		String result = ConvertUtils.convert(calendar.getTime());
		assertEquals("Sun Jan 30 00:00:00 BRST 2000", result);
	}
	
	public void testConvertToDate()
		throws Exception
	{
		Calendar calendar = Calendar.getInstance();
		Date date = (Date) ConvertUtils.convert("30/01/2000", Date.class);
		calendar.setTime(date);
		assertEquals(30, calendar.get(Calendar.DAY_OF_MONTH));
		assertEquals(Calendar.JANUARY, calendar.get(Calendar.MONTH));
		assertEquals(2000, calendar.get(Calendar.YEAR));

		date = (Date) ConvertUtils.convert("dd/MMM/yyyy::20/Fev/2000", Date.class);
		calendar.setTime(date);
		assertEquals(20, calendar.get(Calendar.DAY_OF_MONTH));
		assertEquals(Calendar.FEBRUARY, calendar.get(Calendar.MONTH));
		assertEquals(2000, calendar.get(Calendar.YEAR));
	}

	public void testConvertToDateNoDate()
	{
		Date date = (Date) ConvertUtils.convert("dd/MMM/yyyy::", Date.class);
		assertNull(date);
	}
	
	public void testConvertStillWorksForPrimitives()
		throws Exception
	{
		assertEquals("10",ConvertUtils.convert(new Long(10)));
		assertEquals(new Long(10),ConvertUtils.convert("10", Long.class));
	}
}
