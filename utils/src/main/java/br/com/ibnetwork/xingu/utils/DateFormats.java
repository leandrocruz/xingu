package br.com.ibnetwork.xingu.utils;

import org.apache.commons.lang3.time.FastDateFormat;

public class DateFormats
{
	public static final FastDateFormat yyyyMMdd_HHmmss = FastDateFormat.getInstance("yyyyMMdd.HHmmss");

	public static final FastDateFormat ddMMyyyy        = FastDateFormat.getInstance("dd/MM/yyyy");

	public static final FastDateFormat MMyyyy          = FastDateFormat.getInstance("MM/yyyy");
}
