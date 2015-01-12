package xingu.utils;

public interface ValueProvider<T>
{
	T get(String name);
}
