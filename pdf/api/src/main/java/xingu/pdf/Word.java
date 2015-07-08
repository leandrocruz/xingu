package xingu.pdf;


public interface Word
{
	Coordinate bottomRight()
		throws PdfException;

	Coordinate topLeft()
		throws PdfException;

	int getPageNumber();
}
