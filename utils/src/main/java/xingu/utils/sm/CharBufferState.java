package xingu.utils.sm;

public class CharBufferState
	extends SimpleState<Character, String>
{
	protected StringBuffer sb = new StringBuffer();

	public CharBufferState(String name)
	{
		super(name);
	}

	public CharBufferState(String name, boolean push)
	{
		super(name, push);
	}

	public CharBufferState(String name, boolean push, CharBufferState onEnd)
	{
		super(name, push, onEnd);
	}

	@Override
	public void append(Character value)
	{
		if(value != null)
		{
			sb.append(value);
		}
	}

	@Override
	public void appendResult(String result)
	{
		sb.append(result);
	}

	@Override
	public String collect()
	{
		String result = sb.toString();
		sb = new StringBuffer();
		return result;
	}
}