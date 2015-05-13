package xingu.pdf;

import java.io.FileInputStream;
import java.io.InputStream;

import com.adobe.internal.io.ByteReader;
import com.adobe.internal.io.InputStreamByteReader;
import com.adobe.pdfjt.pdf.document.PDFDocument;
import com.adobe.pdfjt.pdf.document.PDFOpenOptions;

public class ApplicationUtils
{
	public static PDFDocument loadPDFDocument(InputStream is)
		throws Exception
	{
		ByteReader reader = null;
		try
		{
			reader = new InputStreamByteReader(is);
			PDFOpenOptions  options = PDFOpenOptions.newInstance();
			PDFDocument doc = PDFDocument.newInstance(reader, options);
			return doc;
			
		}
		finally
		{
			if(reader != null)
			{
				reader.close();
			}
		}
		
	}
	public static PDFDocument loadPDFDocument(String path)
		throws Exception
	{
		return loadPDFDocument(new FileInputStream(path));
	}
}
