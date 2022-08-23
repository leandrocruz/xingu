package xingu.http.client.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.container.Inject;
import xingu.http.client.*;
import xingu.process.ProcessManager;
import xingu.utils.io.FileNamer;
import xingu.utils.io.FileUtils;
import xingu.utils.io.SerialFileContainer;

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
		return exec(RetryPolicies.dftl());
	}

	@Override
	public HttpResponse exec(RetryPolicy policy)
		throws HttpException
	{
		Logger logger = getLogger();
		String impl   = builder.name();
		int    result = 0 ;
		try
		{
			File         file = getOutputFile(context);
			String      name  = FilenameUtils.getBaseName(file.getName());
			File      headers = new File(file.getParentFile(), name + ".headers");
			List<String> cmd  = builder.buildLine(this, file, headers);
			String join       = builder.join(cmd);
			
			if(logging)
				logger.info("Executing command: {}", join);

			result = applyPolicy(logger, cmd, policy);
			if(result == 0)
			{
				HttpResponse res  = builder.responseFrom(this, file, headers);
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

	private Logger getLogger()
	{
		if(context != null)
		{
			return context.getLogger();
		}
		return LoggerFactory.getLogger(getClass());
	}
	
	public int applyPolicy(Logger logger, List<String> cmd, RetryPolicy policy)
		throws Exception
	{
		int result = pm.exec(cmd);
		if(policy.retry(result))
		{
			logger.info("retrying: " + result);
			return applyPolicy(logger, cmd, policy.update(result));
		}
		return result;
	}

	private File getOutputFile(HttpContext context)
		throws IOException
	{
		File root = getRootDirectory(context);
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
				return "res-" + i + "-" + xingu.utils.StringUtils.orEmpty(name) + ".html";
			}
		});
		return container.next();
	}

	private File getRootDirectory(HttpContext context)
		throws IOException
	{
		if(context != null)
		{
			return context.getRootDirectory();
		}
		File tmp = FileUtils.getTempDirectory();
		return FileUtils.createOrError(tmp, "xingu-http-client");
	}
}
