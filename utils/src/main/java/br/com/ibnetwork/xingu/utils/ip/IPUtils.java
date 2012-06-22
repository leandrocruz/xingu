package br.com.ibnetwork.xingu.utils.ip;

public class IPUtils
{
	public static IPAddress from(String address)
	{
		if(isValidIPv4(address))
		{
			return buildIPv4From(address);
		}
		else if(isValidIPv6(address))
		{
			return buildIPv6From(address);
		}
		return null;
	}

	public static IPv4Address buildIPv4From(String address)
	{
		String octet; 
		String temp = address + ".";
		int[] octets = new int[4];
		int start = 0;
		
		for(int i = 0 ; i < 4 ; i++)
		{
			int idx = temp.indexOf(".", start);
			octet = temp.substring(start, idx);
			start = start + octet.length() + 1;
			octets[i] = Integer.parseInt(octet);
		}
	
		return new IPv4Address(octets);
	}

	public static boolean isValid(String address)
	{
		return isValidIPv4(address) || isValidIPv6(address);
	}

	public static boolean isValidIPv4(String address)
	{
		if (address == null || address.length() == 0)
		{
			return false;
		}

		int octet;
		int octets = 0;

		String temp = address + ".";

		int pos;
		int start = 0;
		while (start < temp.length() && (pos = temp.indexOf('.', start)) > start)
		{
			if (octets == 4)
			{
				return false;
			}
			try
			{
				octet = Integer.parseInt(temp.substring(start, pos));
			}
			catch (NumberFormatException ex)
			{
				return false;
			}
			if (octet < 0 || octet > 255)
			{
				return false;
			}
			start = pos + 1;
			octets++;
		}

		return octets == 4;
	}

	public static boolean isValidIPv6(String address)
	{
		if (address.length() == 0)
		{
			return false;
		}

		int octet;
		int octets = 0;

		String temp = address + ":";
		boolean doubleColonFound = false;
		int pos;
		int start = 0;
		while (start < temp.length() && (pos = temp.indexOf(':', start)) >= start)
		{
			if (octets == 8)
			{
				return false;
			}

			if (start != pos)
			{
				String value = temp.substring(start, pos);

				if (pos == (temp.length() - 1) && value.indexOf('.') > 0)
				{
					if (!isValidIPv4(value))
					{
						return false;
					}

					octets++; // add an extra one as address covers 2 words.
				}
				else
				{
					try
					{
						octet = Integer.parseInt(temp.substring(start, pos), 16);
					}
					catch (NumberFormatException ex)
					{
						return false;
					}
					if (octet < 0 || octet > 0xffff)
					{
						return false;
					}
				}
			}
			else
			{
				if (pos != 1 && pos != temp.length() - 1 && doubleColonFound)
				{
					return false;
				}
				doubleColonFound = true;
			}
			start = pos + 1;
			octets++;
		}

		return octets == 8 || doubleColonFound;
	}

	public static IPAddress buildIPv6From(String address)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public static int compareIPv4(String i1, String i2)
	{
		return 0;
	}

}
