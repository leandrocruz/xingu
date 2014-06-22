package xingu.xls;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
		String count = args[1];
		List<Record> records = ExcelReader.read(file, Integer.valueOf(count), 0);
		
		int i = 0;
		for(Record record : records)
		{
			System.out.println((i++) + ". " + record.get("NOME") + " ("+record.get("DOCUMENTO")+") em " + record.get("COMARCA"));
		}
	}

	public static List<Record> read(String fileName, int cols, int sheetNumber)
		throws Exception
	{
		List<Record> result = new ArrayList<Record>();
		InputStream  is     = new FileInputStream(fileName);
		Workbook     wb     = new HSSFWorkbook(is);
		Sheet        sheet  = wb.getSheetAt(sheetNumber);
		
		String[] meta = new String[cols];
		int i = 0;
		for(Row row : sheet)
		{
			if(i == 0)
			{
				for(int j = 0; j < cols; j++)
				{
					Cell    cell  = row.getCell(j);
					if(cell == null)
					{
						throw new Exception("Row '"+(j+1)+"' is  null. Maybe you should check the 'cols' argument");
					}
					String  value = cell.getStringCellValue();
					meta[j]       = value.trim();
				}				
			}
			else
			{
				Record record = new Record();
				for(int j = 0; j < cols; j++)
				{
					Cell   cell   = row.getCell(j);
					int type = cell.getCellType();
					String value = null;
					switch(type)
					{
						case Cell.CELL_TYPE_BLANK:
							value = StringUtils.EMPTY;
							break;

						case Cell.CELL_TYPE_NUMERIC:
							value = String.valueOf(cell.getNumericCellValue());
							break;

						case Cell.CELL_TYPE_BOOLEAN:
							value = String.valueOf(cell.getBooleanCellValue());
							break;
							
						default:
							value = cell.getStringCellValue();
							break;
					}
					String name   = meta[j];
					record.add(name, value.trim());
				}
				result.add(record);
			}
			i++;
		}
		return result;
	}
}