package xingu.utils.io;

public interface FileNamer<T>
{
	String getName(T t);

	T getParam(String name);
}
