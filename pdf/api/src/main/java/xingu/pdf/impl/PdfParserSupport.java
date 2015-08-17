package xingu.pdf.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import xingu.lang.NotImplementedYet;
import xingu.pdf.Pdf;
import xingu.pdf.PdfParser;

public class PdfParserSupport
	implements PdfParser
{
	@Override
	public Pdf parse(String file)
		throws Exception
	{
		InputStream is = null;
		try
		{
			is = new FileInputStream(new File(file));
			return parse(is);
		}
		finally
		{
			IOUtils.closeQuietly(is);
		}
	}

	@Override
	public Pdf parse(InputStream is)
		throws Exception
	{
		throw new NotImplementedYet();
	}
}
