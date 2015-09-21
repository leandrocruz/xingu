package xingu.lang;

public interface Operation<T>
{
	T execute()
		throws Exception;

	void onError(Throwable t);
}