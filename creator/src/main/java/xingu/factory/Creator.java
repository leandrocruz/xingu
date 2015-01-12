package xingu.factory;

public interface Creator
{
    <T> T create(Class<? extends T> clazz, Object... params)
		throws Exception;
}
