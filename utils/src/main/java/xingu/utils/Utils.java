package xingu.utils;

import xingu.lang.Operation;
import xingu.lang.OperationUnsuccessfulException;
import xingu.lang.RetryException;

public class Utils
{
	public static <T> T tryNTimesThenThrowException(int times, Operation<T> operation)
		throws Exception
	{
		int count  = 0;
		T   result = null;
		while(count < times && result == null)
		{
			count++;
			try
			{
				result = operation.execute();
			}
			catch(RetryException retry)
			{}
			catch(Throwable t)
			{
				throw t;
			}
		}
		if(result == null)
		{
			throw new OperationUnsuccessfulException("Can't compute result after "+count+" executions");
		}
		return result;
	}
}
