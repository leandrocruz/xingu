package br.com.ibnetwork.xingu.lang;

import java.lang.reflect.Constructor;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;

public class Constructors
{
    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> findConstructor(Class<T> clazz, Object... args)
        throws Exception
    {

        if (args == null || args.length == 0)
        {
            return clazz.getDeclaredConstructor();
        }

        Class<?>[] argTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++)
        {
            argTypes[i] = args[i].getClass();
        }

        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors)
        {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length == args.length)
            {
                parameterTypes = ClassUtils.primitivesToWrappers(parameterTypes);
                if (ClassUtils.isAssignable(argTypes, parameterTypes))
                {
                    return (Constructor<T>) constructor;
                }
            }
        }
        throw new Exception("Can't find construtor on " + clazz.getName()
                + " that matches " + ArrayUtils.toString(argTypes));
    }

}
