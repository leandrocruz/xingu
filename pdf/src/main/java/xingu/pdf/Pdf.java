package xingu.pdf;

public interface Pdf
{
	Line getLine(int lineNumber);

	Line find(String text);
}