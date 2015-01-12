package xingu.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ValidatorContext
{
	private Map<String, ValidatorResult> map = new HashMap<String, ValidatorResult>();
	
	public int getErrorCount()
    {
		Set<String> keys = map.keySet();
		int count = 0;
		for (String key : keys)
        {
			ValidatorResult result = map.get(key);
			if(!result.isValid())
			{
				count++;
			}
        }
		return count;
    }

	public void put(String fieldName, ValidatorResult result)
    {
		map.put(fieldName, result);
    }

	public ValidatorResult get(String fieldName)
    {
	    return map.get(fieldName);
    }
	
	public String[] getFields()
	{
		Set<String> keys = map.keySet();
		String[] result = new String[keys.size()];
		int i = 0;
		for (String fieldName : keys)
        {
			result[i++] = fieldName;
        }
		return result;
	}
}
