package xingu.inhaka;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class Inhaka
{
	@SuppressWarnings("unchecked")
	public static <T> T enhance(Class<? extends T> clazz, final T t)
		throws Exception
	{
		return (T) Enhancer.create(clazz, new MethodInterceptor()
		{
			@Override
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
				throws Throwable
			{
				System.err.print(t.getClass().getSimpleName() + "." + method.getName() + "() -> ");
				Object result = proxy.invoke(t, args);
				System.err.println(result == null ? "NULL" : result.getClass().getSimpleName());
				return result;
			}
		});
	}
}
