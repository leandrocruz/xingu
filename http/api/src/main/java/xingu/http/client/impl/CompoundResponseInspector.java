package xingu.http.client.impl;

import xingu.http.client.HttpResponse;
import xingu.http.client.ResponseInspector;

public class CompoundResponseInspector
	implements ResponseInspector
{
	private ResponseInspector[]	inspectors;

	public CompoundResponseInspector(ResponseInspector...inspectors)
	{
		this.inspectors = inspectors;
	}
	
	@Override
	public void throwErrorIf(HttpResponse res)
		throws Exception
	{
		for(ResponseInspector inspector : inspectors)
		{
			inspector.throwErrorIf(res);
		}
	}
}
