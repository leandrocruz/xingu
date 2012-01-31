package br.com.ibnetwork.xingu.utils.annotation;

import junit.framework.TestCase;

public class AnnotationTest
    extends TestCase
{
	public void testGetAnnatationOnParent()
		throws Exception
	{
		Sample sample = Bean.class.getAnnotation(Sample.class);
		assertEquals("onBaseClass",sample.value());
	}
}
