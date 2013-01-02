package xingu.journal;

public interface Journal
{
	void append(String string)
		throws Exception;

	void append(String string, boolean withSeparator)
		throws Exception;
}
