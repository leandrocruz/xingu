package xingu.xls;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelReader
{
	public static void main(String[] args)
		throws Exception
	{
		String file  = args[0];
		List<Record> records = ExcelReader.read(file, 0, true);
		System.out.println(records.size() + " Registros encontrados");
	}

	public static List<Record> read(String fileName)
		throws Exception
	{
		return read(fileName, 0, true);
	}

	public static List<Record> read(InputStream is)
		throws Exception
	{
		return read(is, 0, true);
	}

	public static List<Record> read(String fileName, int sheetNumber, boolean readHeader)
		throws Exception
	{
		InputStream is = new FileInputStream(fileName);
		return read(is, sheetNumber, readHeader);
	}

	public static List<Record> read(InputStream is, int sheetNumber, boolean readHeader)
		throws Exception
	{
		List<Record> result = new ArrayList<Record>();
		Workbook     wb     = new HSSFWorkbook(is);
		Sheet        sheet  = wb.getSheetAt(sheetNumber);
		
		int    end    = sheet.getLastRowNum();
		Header header = null;
		
		for(int i = 0; i < end; i++)
		{
			Row row = sheet.getRow(i);
			if(header == null && readHeader)
			{
				header = readHeader(row);
				continue;
			}

			Record record = toRecord(header, row);
			if(record != null)
			{
				result.add(record);
			}
		}
		return result;
	}

	public static Header readHeader(Row row)
	{
		Header result = new Header();
		short  start  = row.getFirstCellNum();
		short  end    = row.getLastCellNum();

		for(int i = start; i < end; i++)
		{
			Cell   cell  = row.getCell(i);
			String value = valueFrom(cell);
			value = StringUtils.trimToNull(value);
			if(value != null)
			{
				result.add(i, value);
			}
		}

		return result;
	}

	public static Record toRecord(Header header, Row row)
		throws Exception
	{
		if(row == null)
		{
			return null;
		}

		short start = row.getFirstCellNum();
		short end   = row.getLastCellNum();

		if(start < 0 || end < 0)
		{
			return null;
		}
		
		Record record = new Record();
		for(int i = start; i < end; i++)
		{
			Cell cell = row.getCell(i);
			if(cell != null)
			{
				String value = valueFrom(cell);
				String name  = header != null ? header.nameFor(i) : String.valueOf(i);
				System.out.print("[" + name + "] " + value + "\t");
				record.add(name, value.trim());
			}
		}
		System.out.println("");
		
		return record;
	}

	public static String valueFrom(Cell cell)
	{
		if(cell == null)
		{
			return null;
		}
		
		int  type = cell.getCellType();
		switch(type)
		{
			case Cell.CELL_TYPE_BLANK:
				return StringUtils.EMPTY;

			case Cell.CELL_TYPE_NUMERIC:
				return String.valueOf(cell.getNumericCellValue());

			case Cell.CELL_TYPE_BOOLEAN:
				return String.valueOf(cell.getBooleanCellValue());
				
			default:
				return cell.getStringCellValue();
		}
	}
}

class Header {

	private Map<Integer, String> nameByColumn = new HashMap<Integer, String>();
	
	public String nameFor(int col)
	{
		return nameByColumn.get(col);
	}

	public void add(int col, String name)
	{
		nameByColumn.put(col, name);
	}
}