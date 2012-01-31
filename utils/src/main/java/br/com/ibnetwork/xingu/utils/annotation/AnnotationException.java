package br.com.ibnetwork.xingu.utils.annotation;

public class AnnotationException
    extends RuntimeException
{

	public AnnotationException(String message, Throwable t)
    {
		super(message,t);
    }

	public AnnotationException(String message)
    {
		super(message);
    }

}
