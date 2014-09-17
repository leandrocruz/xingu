package xingu.http.client.impl;

import java.util.HashMap;
import java.util.Map;

import xingu.http.client.HttpException;
import xingu.http.client.HttpResponse;
import xingu.http.client.ResponseInspector;

public class ResponseCodeInspector
	implements ResponseInspector
{
	private int		expected;

	private String	msg;
	
	private static final Map<Integer, ResponseCodeInspector> byCode = new HashMap<Integer, ResponseCodeInspector>();

	private ResponseCodeInspector(int code, String msg)
	{
		this.expected = code;
		this.msg      = msg;
	}

	public static final ResponseCodeInspector forCode(int code, String msg)
	{
		if(msg == null)
		{
			ResponseCodeInspector result = byCode.get(code);
			if(result == null)
			{
				result = new ResponseCodeInspector(code, null);
				byCode.put(code, result);
			}
			return result;
		}
		return new ResponseCodeInspector(code, msg);
	}
	
	
	private String getErrorMessage(int code)
	{
		if(msg != null)
		{
			return msg;
		}
		return "Response code mismatch. Expected: " + expected + ", but was: " + code;
	}

	@Override
	public void throwErrorIf(HttpResponse res)
		throws Exception
	{
		int code = res.getCode();
		if(expected != code)
		{
			String msg = getErrorMessage(code);
			throw new HttpException(msg);
		}
	}
}