package xingu.utils;

import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomIdGenerator
{
	public static String next(int size)
	{
		return next(size, "-");
	}

	public static String next(int size, String separator)
	{
		return DateFormats.yyyyMMdd_HHmmss.format(new Date()) 
				+ separator 
				+ RandomStringUtils.randomAlphanumeric(size).toLowerCase();
	}
}
