package xingu.xls;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;

import xingu.lang.NotImplementedYet;

public class ExcelUtils
{
	public static Date toDate(Cell cell, boolean required)
	{
		if(cell == null)
		{
			if(required)
			{
				throw new NotImplementedYet("blank field");
			}
			return null;
		}
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		Date value = cell.getDateCellValue();
		if(required && value == null)
		{
			throw new NotImplementedYet("blank field");
		}
		return value;
	}
	
	public static String toString(Cell cell, boolean required)
	{
		if(cell == null)
		{
			if(required)
			{
				throw new NotImplementedYet("blank field");
			}
			return StringUtils.EMPTY;
		}
		cell.setCellType(Cell.CELL_TYPE_STRING);
		String value = cell.getStringCellValue();
		if(required && StringUtils.isEmpty(value))
		{
			throw new NotImplementedYet("blank field");
		}
		return value;
	}

	public static double toNumber(Cell cell, boolean required)
	{
		if(cell == null)
		{
			if(required)
			{
				throw new NotImplementedYet("blank field");
			}
			return 0;
		}
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		double value = cell.getNumericCellValue();
		if(required && value <= 0.0)
		{
			throw new NotImplementedYet("blank field");
		}
		return value;
	}
}