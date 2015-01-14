package xingu.utils.sm;

public class SimpleState<T, R>
	implements State<T, R>
{
	protected String		name;

	protected T				toMatch;	// null for any value

	protected State<T, R>	next;

	protected State<T, R>	fallback;

	protected State<T, R>	onEnd;

	protected boolean		push;

	public SimpleState(String name)
	{
		this(name, false, null);
	}

	public SimpleState(String name, boolean push)
	{
		this(name, push, null);
	}

	public SimpleState(String name, boolean push, State<T, R> onEnd)
	{
		this.name  = name;
		this.push  = push;
		this.onEnd = onEnd;
	}

	@Override
	public String name()
	{
		return name;
	}

	@Override
	public State<T, R> on(T value)
	{
		if(toMatch == null && next != null)
		{
			//any()
			return fw(value, next);
		}

		if(value == null)
		{
			return whenNull();
		}
		
		if(value.equals(this.toMatch))
		{
			if(next != null)
			{
				return fw(value, next);
			}
			else
			{
				return whenNextNull(value);
			}
		}

		return whenNotEquals(value);
	}

	@Override
	public State<T, R> end()
	{
		return onEnd == null ? this : fw(null, onEnd);
	}

	protected State<T, R> fw(T value, State<T, R> to)
	{
		if(push)
		{
			R result = collect();
			to.appendResult(result);
		}

		to.append(value);

		return to;
	}
	
	protected State<T, R> whenNotEquals(T value)
	{
		append(value);
		if(fallback != null && fallback != this)
		{
			R result = collect();
			fallback.appendResult(result);
			return fallback;
		}
		return this;
	}

	public void append(T value)
	{}

	public void appendResult(R result)
	{}

	protected State<T, R> whenNull()
	{
		return this;
	}

	protected State<T, R> whenNextNull(T t)
	{
		return this;
	}

	@Override
	public void when(T value, State<T, R> next)
	{
		this.toMatch = value;
		this.next  = next;
	}

	@Override
	public void when(T value, State<T, R> next, State<T, R> fallback)
	{
		this.toMatch    = value;
		this.next     = next;
		this.fallback = fallback;
	}

	@Override
	public void any(State<T, R> next)
	{
		this.next = next;
	}

	@Override
	public String toString()
	{
		return "[" + name + "] " + toMatch + " -> " + next.name();
	}

	@Override
	public R collect()
	{
		return null;
	}
}