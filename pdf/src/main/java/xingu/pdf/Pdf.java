package xingu.pdf;

import java.io.Writer;
import java.util.List;
import java.util.regex.Pattern;

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

	String get(Pattern pattern);
}