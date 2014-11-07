package xingu.http.client.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static final Logger	logger	= LoggerFactory.getLogger(SimpleHttpRequest.class);

	public SimpleHttpRequest(String uri, String method)
	{
		super(uri, method);
	}

	@Override
	public HttpResponse exec()
		throws HttpException
	{
		String impl   = builder.name();
		int    result = 0 ;
		try
		{
			File         file = getOutputFile();
			List<String> cmd  = builder.buildLine(this, file);
			logger.info("Executing command: {}", StringUtils.join(cmd, " "));

			result = execCmd(cmd, 3);
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
	
	public int execCmd(List<String> cmd, int retryCount)
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
			return execCmd(cmd, retryCount - 1);
		}

		return result;
	}

	private File getOutputFile()
		throws IOException
	{
		final String impl = builder.name();
		File  tmp         = org.apache.commons.io.FileUtils.getTempDirectory();
		if(StringUtils.isNotEmpty(ndc))
		{
			File root = FileUtils.createOrError(tmp, "xingu-http-client" + File.separator + ndc);
			SerialFileContainer container = new SerialFileContainer(root, new FileNamer<Integer>() {
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
		return File.createTempFile(impl + "-http-response-", ".html");
	}
}
