package xingu.utils.clone;


public interface Cloner
{
	<T> T deepClone(T t)
		throws CloneException;

	<T> T deepCloneWithContext(CloningContext ctx, T original)
		throws CloneException;

	void addHandler(Class<?> clazz, ReferenceHandler handler);

}
