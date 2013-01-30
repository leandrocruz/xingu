package xingu.journal.impl;



public class PrintToSysOutJournal
	extends JournalSupport
{
	@Override
	public void append(Level level, String string)
	{
		System.err.println(level + " " + string);
	}
}
