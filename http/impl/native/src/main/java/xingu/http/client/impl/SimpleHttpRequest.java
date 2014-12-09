package xingu.http.client.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import xingu.http.client.HttpContext;
import xingu.http.client.HttpException;
import xingu.http.client.HttpResponse;
import xingu.process.ProcessManager;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.utils.io.FileNamer;
import br.com.ibnetwork.xingu.utils.io.FileUtils;
import br.com.ibnetwork.xingu.utils.io.SerialFileContainer;

public class SimpleHttpRequest
	extends HttpRequestSupport
{
	@Inject
	private ProcessManager		pm;

	@Inject
	private CommandLineBuilder	builder;

	public SimpleHttpRequest(String uri, String method)
	{
		super(uri, method);
	}

	@Override
	public HttpResponse exec()
		throws HttpException
	{
		Logger logger = context.getLogger();
		String impl   = builder.name();
		int    result = 0 ;
		try
		{
			File         file = getOutputFile(context);
			List<String> cmd  = builder.buildLine(this, file);
			logger.info("Executing command: {}", StringUtils.join(cmd, " "));

			result = execCmd(logger, cmd, 3);
			if(result == 0)
			{
				HttpResponse res  = builder.responseFrom(this, file);
				check(res);
				return res;
			}
		}
		catch(Exception e)
		{
			throw new HttpException(e);
		}

		throw new HttpException(impl + " error: " + result);
	}
	
	public int execCmd(Logger logger, List<String> cmd, int retryCount)
		throws Exception
	{
		int result = pm.exec(cmd);
		if(result == 0)
		{
			return 0;
		}

		/* 
		 * 6  - Couldn't resolve host. 
		 * 7  - Failed to connect to host 
		 * 28 - Operation timeout
		 */
		if(retryCount > 0
				&& (6 == result || 7 == result || 28 == result))
		{
			logger.info("retrying: " + result);
			return execCmd(logger, cmd, retryCount - 1);
		}

		return result;
	}

	private File getOutputFile(HttpContext context)
		throws IOException
	{
		File root = context.getRootDirectory();
		File dir  = FileUtils.createOrError(root, "http-responses");
		SerialFileContainer container = new SerialFileContainer(dir, new FileNamer<Integer>() {
			@Override
			public Integer getParam(String name)
			{
				int    start = name.indexOf("-") + 1;
				int    end   = name.indexOf("-", start);
				String s     = name.substring(start, end);
				return Integer.parseInt(s);
			}
			
			@Override
			public String getName(Integer i)
			{
				return "res-" + i + "-" + br.com.ibnetwork.xingu.utils.StringUtils.orEmpty(name) + ".html";
			}
		});
		return container.next();
	}
}
