package xingu.utils;

public class Tuple<K,V>
{
	private K	k;
	private V	v;

	public Tuple(K k, V v)
	{
		this.k = k;
		this.v = v;
	}

	public K getKey()
	{
		return k;
	}
	
	public V getValue()
	{
		return v;
	}

	@Override
	public String toString()
	{
		return "[" + k + "]=[" + v + "]";
	}
}