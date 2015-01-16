package xingu.http.client.impl;

import xingu.http.client.HttpProgressListener;

public abstract class StepListener
	implements HttpProgressListener
{
	protected int step;
	
	protected int count = 1;
	
	public StepListener(int step)
	{
		this.step = step;
	}

	@Override
	public void on(long total, long progress)
		throws Exception
	{
		if(isMultiple(total, progress))
		{
			log(total, progress);
		}
	}

	private boolean isMultiple(long total, long progress)
	{
        double value = ((double) progress/(double) total) * 100;
        int threshold = count * step;
		if(value < threshold)
		{
			return false;
		}
		count++;
		return true;
	}

	protected abstract void log(long total, long progress);
}
