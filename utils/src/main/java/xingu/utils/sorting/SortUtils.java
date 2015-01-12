package xingu.utils.sorting;

import org.apache.commons.beanutils.MethodUtils;

public class SortUtils
{
	public static void bubbleSort(Object[] array, String getterName) 
		throws Exception
	{
		for (int i = 0; i < array.length; i++)
		{
			for (int j = array.length-1; j > i; j--)
			{	
				Object obj1 = array[j-1];
				Number val1 = (Number) MethodUtils.invokeMethod(obj1,getterName,null);
				long long1 = val1.longValue();
				Object obj2 = array[j];
				Number val2 = (Number) MethodUtils.invokeMethod(obj2,getterName,null);
				long long2 = val2.longValue();
				if (long1 > long2)
				{
					array[j-1] = obj2;
					array[j] = obj1;
				}
			}
		}
	}
}
