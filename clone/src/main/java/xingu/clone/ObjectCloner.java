package xingu.clone;


public interface ObjectCloner
{
	Object build(Object source, String name)
		throws Exception;
}
