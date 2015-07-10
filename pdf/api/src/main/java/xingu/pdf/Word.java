package xingu.pdf;

public interface Word
{
	Coordinate bottomRight()
		throws PdfException;

	Coordinate topLeft()
		throws PdfException;

	int pageNumber();
	
	String text();
}
