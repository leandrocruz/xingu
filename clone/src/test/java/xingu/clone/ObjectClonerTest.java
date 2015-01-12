package xingu.clone;

import org.junit.Test;

import xingu.container.Inject;
import xingu.container.XinguTestCase;

public class ObjectClonerTest
	extends XinguTestCase
{
	@Inject
	private ObjectCloner builder;

	@Test
	public void test()
		throws Exception
	{
		Object source = new Sample(1, "s");
		Object copy = builder.build(source, "SampleClone");
	}
}

class Sample
{

	private int i;
	
	private Integer i2;
	
	private String string;

	public Sample(int i, String string)
	{
		this.i = i;
		this.i2 = new Integer(i);
		this.string = string;
	}
}
