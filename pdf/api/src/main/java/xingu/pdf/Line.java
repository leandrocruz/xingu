package xingu.pdf;

import java.util.List;

public interface Line
{
	String asText();

	int getNumber();
	void setNumber(int i);

	int getPage();

	Word getWord(int i);

	List<Word> getWords();

}