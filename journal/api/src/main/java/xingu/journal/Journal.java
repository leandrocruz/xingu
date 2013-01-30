package xingu.journal;


public interface Journal
{
	enum Level {DEBUG, INFO, WARN, ERROR};
	
	void debug(String string)
		throws Exception;

	void debug(String pattern, Object... args)
		throws Exception;

	void info(String string)
		throws Exception;

	void info(String pattern, Object... args)
			throws Exception;
}