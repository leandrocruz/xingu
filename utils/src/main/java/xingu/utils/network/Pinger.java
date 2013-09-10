package xingu.utils.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class Pinger
{
	private Pattern pattern = Pattern.compile("(\\d+) packets transmitted, (\\d+) received,( \\+(\\d+) errors,)? (\\d+)% packet loss, time (\\d+)ms");

	public static final void main(String[] args)
		throws Exception
	{
		String addr = "192.168.0.1";
		if(args.length > 0)
		{
			addr = args[0];
		}
		PingResult result = new Pinger().ping(addr);
		System.out.println(result);
	}

	public PingResult ping(String addr)
		throws IOException
	{
		return ping(addr, 3);
	}
	
	public PingResult ping(String addr, int count)
		throws IOException
	{
		ProcessBuilder builder   = new ProcessBuilder("ping", "-c" + count, addr);
		builder.redirectErrorStream(true);
		Process        process   = builder.start();
		InputStream    is        = process.getInputStream();
		BufferedReader reader    = new BufferedReader(new InputStreamReader(is));

		String line;
		PingResult   result = null;
		boolean      flag   = false;
		while((line = reader.readLine()) != null)
		{
			//System.err.println(line);
			if(!flag && line.startsWith("---"))
			{
				flag = true;
				continue;
			}
			if(flag)
			{
				Matcher matcher = pattern.matcher(line);
				if(matcher.matches())
				{
					int    transmitted = Integer.parseInt(matcher.group(1));
					int    received    = Integer.parseInt(matcher.group(2));
					int    loss        = Integer.parseInt(matcher.group(5));
					int    time        = Integer.parseInt(matcher.group(6));

					String err    = matcher.group(4);
					int    errors = StringUtils.isEmpty(err) ? 0 : Integer.parseInt(err);

					IOUtils.closeQuietly(reader);
					return new PingResult(transmitted, received, loss, time, errors);
				}
			}
		}

		IOUtils.closeQuietly(reader);
		
		return result;
	}
}