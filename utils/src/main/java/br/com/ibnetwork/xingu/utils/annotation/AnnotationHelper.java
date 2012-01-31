package br.com.ibnetwork.xingu.utils.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import br.com.ibnetwork.xingu.utils.FieldUtils;

public class AnnotationHelper
{
	public static void print(AnnotatedElement element)
	{
		Annotation[] annotations = element.getAnnotations();
		for (Annotation annotation : annotations)
        {
			System.out.println(annotation);
        }
	}

	public static void print(AnnotatedElement[] elements)
    {
		for (AnnotatedElement element : elements)
        {
	        AnnotationHelper.print(element);
        }
    }
	
	public static Annotation getAnnotationByType(AnnotatedElement element, Class annotationType)
	{
		Annotation[] annotations = element.getAnnotations();
		for (Annotation annotation : annotations)
        {
			if(annotation.annotationType().equals(annotationType))
			{
				return annotation;
			}
        }
		return null;
	}
	
	public static Annotation getFieldAnnotationByType(Class beanClass, String fieldName, Class annotationType)
		throws AnnotationException
	{
		try
        {
	        Field field = FieldUtils.getField(beanClass,fieldName);
	        if(field == null)
	        {
	        	return null;
	        }
	        return getAnnotationByType(field,annotationType);
        }
        catch (Exception e)
        {
	        throw new AnnotationException("Error retrieving annotation: "
	        		+annotationType.getName()
	        		+" for field: "+fieldName
	        		+" beanClass: "+beanClass.getName(),e);
        }
	}

}
