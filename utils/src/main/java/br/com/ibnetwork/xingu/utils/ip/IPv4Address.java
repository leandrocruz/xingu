package br.com.ibnetwork.xingu.utils.ip;

import java.io.Serializable;

public class IPv4Address
	implements IPAddress, Serializable, Comparable<IPv4Address>
{
	private int[] octets;
	
	public IPv4Address(int i, int j, int k, int l)
	{
		octets = new int[4];
		octets[0] = i;
		octets[1] = j;
		octets[2] = k;
		octets[3] = l;
	}

	public IPv4Address(int[] octets)
	{
		this.octets = octets;
	}

	@Override
	public boolean isValid()
	{
		if(octets == null || octets.length != 4)
		{
			return false;
		}
		
		for (int i = 0; i < 4; i++)
		{
			int octet = octets[i];
			if (octet < 0 || octet > 255)
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof IPv4Address))
		{
			return false;
		}
		
		IPv4Address other = (IPv4Address) obj;
		for (int i = 0; i < 4; i++)
		{
			int o1 = octets[i];
			int o2 = other.octets[i];
			if(o1 != o2)
			{
				return false;
			}
		}
		
		return true;
	}

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	@Override
	public int compareTo(IPv4Address other)
	{
		for (int i = 0; i < 4; i++)
		{
			int o1 = octets[i];
			int o2 = other.octets[i];
			if(o1 != o2)
			{
				return o1 - o2;
			}
		}
		return 0;
	}

	@Override
	public String getAddress()
	{
		return octets[0] + "." + octets[1] + "." + octets[2] + "." + octets[3]; 
	}
	
	@Override
	public String toString()
	{
		return getAddress();
	}
}
