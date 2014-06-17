package xingu.pdf;

import java.io.FileInputStream;

import com.adobe.internal.io.ByteReader;
import com.adobe.internal.io.InputStreamByteReader;
import com.adobe.pdfjt.pdf.document.PDFDocument;
import com.adobe.pdfjt.pdf.document.PDFOpenOptions;

public class ApplicationUtils
{

	public static PDFDocument loadPDFDocument(String path)
		throws Exception
	{
		FileInputStream is      = new FileInputStream(path);
		ByteReader      reader  = new InputStreamByteReader(is);
		PDFOpenOptions  options = PDFOpenOptions.newInstance();
		PDFDocument doc = PDFDocument.newInstance(reader, options);
		reader.close();
		return doc;
	}

}
