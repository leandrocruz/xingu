package xingu.pdf;

import java.util.List;

public interface Line
{
	String text();

	int number();
	void number(int i);

	int page();

	Word word(int i);
	Word lastWord();
	List<Word> words();
}