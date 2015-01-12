package xingu.utils.clone;

import java.util.Set;

public class WithSet<T>
{
	private final Set<T> set;

	public WithSet(Set<T> set)
	{
		this.set = set;
	}

	public Set<T> set()
	{
		return set;
	}
}
