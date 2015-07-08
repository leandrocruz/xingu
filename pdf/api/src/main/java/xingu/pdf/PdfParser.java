package xingu.pdf;

import java.io.InputStream;

public interface PdfParser
{
	Pdf parse(InputStream is)
		throws Exception;

	Pdf parse(String file)
		throws Exception;
}
