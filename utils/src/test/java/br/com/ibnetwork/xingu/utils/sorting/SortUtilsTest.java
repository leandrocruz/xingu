package br.com.ibnetwork.xingu.utils.sorting;

import junit.framework.TestCase;

/**
 * @author leandro
 *
 */
public class SortUtilsTest
	extends TestCase
{
	private ValueHolder[] array;
	
	public SortUtilsTest()
	{
		super();
	}

	public SortUtilsTest(String testName)
	{
		super(testName);
	}
	
	public void setUp()
	{
		array = new ValueHolder[]{new ValueHolder(2),new ValueHolder(5),new ValueHolder(1),new ValueHolder(5)};	
	}
	
	public void testBubbleSorting()
		throws Exception
	{
		int previous = 0;
		SortUtils.bubbleSort(array,"getValue");
		for (int i = 0; i < array.length; i++)
        {
            ValueHolder element = array[i];
            int value = element.getValue(); 
            assertTrue( value >= previous);
			previous = value;
        }		
	}

	

}

class ValueHolder
{
	int value;
	
	ValueHolder(int value)
	{
		this.value = value;
	}
	
	public int getValue()
	{
		return value;	
	}
}