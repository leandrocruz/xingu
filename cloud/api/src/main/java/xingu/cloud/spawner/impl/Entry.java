package xingu.cloud.spawner.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import xingu.cloud.spawner.Surrogate;

public class Entry
{
	public Surrogate				surrogate;

	private Map<String, Boolean>	lockByBundle	= new HashMap<String, Boolean>();
	
	public Entry(Surrogate surrogate)
	{
		this.surrogate = surrogate;
	}

	public synchronized boolean tryLockFor(String bundle)
	{
		boolean locked = isLockedFor(bundle);
		if(!locked)
		{
			lockByBundle.put(bundle, Boolean.TRUE);
			return true;
		}
		return false;
	}

	public synchronized void unlock(String bundle)
	{
		lockByBundle.put(bundle, Boolean.FALSE);
	}

	public synchronized boolean isLockedFor(String bundle)
	{
		Boolean locked = lockByBundle.get(bundle);
		return locked == null ? false : locked; 
	}

	public synchronized boolean isLocked()
	{
		Set<String> keys = lockByBundle.keySet();
		for(String key : keys)
		{
			if(isLockedFor(key))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer("Surrogate s#")
		.append(surrogate.getId())
		.append(" locked to [");
		
		boolean replace = false;
		
		Set<String> keys = lockByBundle.keySet();
		for(String key : keys)
		{
			boolean lockedFor = isLockedFor(key);
			if(lockedFor)
			{
				replace = true;
				sb.append(key).append(",");
			}
		}

		if(replace)
		{
			sb.setCharAt(sb.length() - 1, ']');
		}
		else
		{
			sb.append("]");
		}
		return sb.toString();
	}
}