package xingu.pdf;

import java.io.Writer;
import java.util.List;

public interface Pdf
{
	String getFile();
	
	Line getLine(int lineNumber);

	Line getLine(int lineNumber, int page);
	
	List<Line> getLines();

	Line find(String text, int page);

	void printTo(Writer writer)
		throws Exception;
	
	int getPageCount();

	String getText();
}