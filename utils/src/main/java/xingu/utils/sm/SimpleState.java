package xingu.utils.sm;

import java.util.ArrayList;
import java.util.List;

public class SimpleState<T, R>
	implements State<T, R>
{
	protected String		name;

	protected State<T, R>	fallback;

	protected State<T, R>	onEnd;

	protected boolean		push;

	private List<Entry<T,R>> entries = new ArrayList<>();
	
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
		if(entries.size() == 0)
		{
			if(fallback != null)
			{
				//any
				return fw(value, fallback);
			}
			return this;
		}

		if(value == null)
		{
			return this;
		}

		for(Entry<T, R> entry : entries)
		{
			boolean equals = value.equals(entry.toMatch);
			if(equals)
			{
				if(entry.next != null)
				{
					return fw(value, entry.next);
				}
				else
				{
					return this;
				}
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

	@Override
	public State<T, R> when(T value, State<T, R> next)
	{
		entries.add(new Entry<T,R>(value, next));
		return this;
	}

	@Override
	public State<T, R> fallback(State<T, R> fallback)
	{
		this.fallback = fallback;
		return this;
	}

	@Override
	public String toString()
	{
		return name;
	}

	public void append(T value)
	{}

	public void appendResult(R result)
	{}

	@Override
	public R collect()
	{
		return null;
	}
}

class Entry<T, R>
{
	T			toMatch;	// null for any value

	State<T, R>	next;

	public Entry(T value, State<T, R> next)
	{
		this.toMatch = value;
		this.next    = next;
	}
}