package br.com.ibnetwork.xingu.utils.ip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class IpChecker
{
	public static String getIp()
	{
		BufferedReader in = null;
		try
		{
			URL url = new URL("http://checkip.amazonaws.com");
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			return in.readLine();
		}
		catch(Throwable t)
		{
			return null;
		}
		finally
		{
			if(in != null)
			{
				try
				{
					in.close();
				}
				catch(IOException e)
				{}
			}
		}
	}
}