package br.com.ibnetwork.xingu.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class ObjectUtils
{
	public static Class<?> loadClass(String className)
		throws NestableRuntimeException
	{
		ClassLoader cl = Thread.currentThread().getContextClassLoader();	
		return loadClass(className, cl);
	}
	
	public static Class<?> loadClass(String className, ClassLoader cl)
		throws NestableRuntimeException
	{
		Class<?> clazz;
        try
        {
	        clazz = cl.loadClass(className);
        }
        catch (ClassNotFoundException e)
        {
        	throw new NestableRuntimeException("Error loading class ["+className+"]",e);
        }
		return clazz;
	}
	
    public static Object getInstance(String className)
		throws NestableRuntimeException
	{
    	ClassLoader cl = Thread.currentThread().getContextClassLoader();
    	return getInstance(className, cl);
	}

    public static Object getInstance(String className, ClassLoader cl)
		throws NestableRuntimeException
	{
    	try
        {
        	Class<?> clazz = cl.loadClass(className);
            Object obj = clazz.newInstance();
            return obj;
        }
        catch (Exception e)
        {
            throw new NestableRuntimeException("Error creating object of class ["+className+"]",e);
        }
	}

    public static Object getInstance(Class<?> clazz)
    	throws NestableRuntimeException
    {
        try
        {
            return clazz.newInstance();
        }
        catch (Exception e)
        {
            throw new NestableRuntimeException("Error creating object of class ["+clazz.getName()+"]",e);
        }
    }

    public static String caller()
    {
        StackTraceElement el = new Throwable().getStackTrace()[2];
        return el.getClassName() + "." + el.getMethodName() + "()";
    }

    public static Object deepCopy(Object original) 
            throws IOException, ClassNotFoundException
        {
            ObjectInputStream ois;
            ObjectOutputStream oos;
            ByteArrayInputStream bais;
            ByteArrayOutputStream baos;
            byte[] data;
            Object copy;

            // write object to bytes
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(original);
            oos.close();

            // get the bytes
            data = baos.toByteArray();

            // construct an object from the bytes
            bais = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(bais);
            copy = ois.readObject();
            ois.close();
            return copy;
        }

}
