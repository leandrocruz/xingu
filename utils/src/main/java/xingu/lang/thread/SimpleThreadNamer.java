package xingu.lang.thread;

public class SimpleThreadNamer
	implements ThreadNamer
{
	private final String prefix;

	public SimpleThreadNamer(String prefix)
	{
		this.prefix = prefix;
	}
	
	@Override
	public String nameFor(int threadNumber)
	{
		return prefix + " #" + threadNumber;
	}
}
