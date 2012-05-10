package br.com.ibnetwork.xingu.utils;

import java.util.Calendar;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class TimeUtils
{
	public enum Unit {
		DAY, WEEK, MONTH, YEAR
	}

	public static long toMillis(String value)
	{
		if (StringUtils.isEmpty(value))
		{
			return -1;
		}

		String number = number(value);
		long factor = getFactor(value);
		long v = Long.parseLong(number);
		return v * factor;
	}

	private static long getFactor(String value)
	{
		long factor = 0;
		if (value.endsWith("ms"))
		{
			factor = 1;
		}
		else if (value.endsWith("s"))
		{
			factor = 1000;
		}
		else if (value.endsWith("m"))
		{
			factor = 1000 * 60;
		}
		else if (value.endsWith("h"))
		{
			factor = 1000 * 60 * 60;
		}
		else if (value.endsWith("d"))
		{
			factor = 1000 * 60 * 60 * 24;
		}
		return factor;
	}

	private static String number(String value)
	{
		if (value.endsWith("ms"))
		{
			return value.substring(0, value.length() - 2);
		}
		else
		{
			return value.substring(0, value.length() - 1);
		}
	}

	public static String toSeconds(long millis)
	{
		return toString(millis, "s");
	}

	public static String toMinutes(long millis)
	{
		return toString(millis, "m");
	}

	public static String toHours(long millis)
	{
		return toString(millis, "h");
	}

	public static String toDays(long millis)
	{
		return toString(millis, "d");
	}

	public static String toString(long millis, String unit)
	{
		double factor = getFactor(unit);
		return ((double) millis) / factor + unit;
	}

	public static Calendar date(int year, int month, int day)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}

	public static String monthName(int i)
	{
		switch (i)
		{
			case 0:
				return "JANUARY";
			case 1:
				return "FEBRUARY";
			case 2:
				return "MARCH";
			case 3:
				return "APRIL";
			case 4:
				return "MAY";
			case 5:
				return "JUNE";
			case 6:
				return "JULY";
			case 7:
				return "AUGUST";
			case 8:
				return "SEPTEMBER";
			case 9:
				return "OCTOBER";
			case 10:
				return "NOVEMBER";
			case 11:
				return "DECEMBER";
			default:
				throw new NotImplementedYet();
		}
	}
}
