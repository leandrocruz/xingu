package xingu.http.client.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.handler.codec.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.http.client.ConnectionRefused;
import xingu.http.client.Cookies;
import xingu.http.client.HttpException;
import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;
import xingu.http.client.NameValue;
import xingu.process.ProcessManager;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.io.FileNamer;
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

			result = pm.exec(cmd);
			if(result == 0)
			{
				return builder.responseFrom(this, file);
			}
		}
		catch(Exception e)
		{
			throw new HttpException(e);
		}

		if(7 == result /* TODO: curl specific */)
		{
			throw new ConnectionRefused("Failed connect to '"+this.getUri()+"'");
		}
		throw new NotImplementedYet("ERROR executing "+impl+" processes: " + result);
	}

	private File getOutputFile()
		throws IOException
	{
		final String impl = builder.name();
		File  tmp         = org.apache.commons.io.FileUtils.getTempDirectory();
		if(StringUtils.isNotEmpty(ndc))
		{
			File    root    = new File(tmp, ndc);
			root.mkdirs();

			SerialFileContainer container = new SerialFileContainer(root, new FileNamer<Integer>() {
				@Override
				public Integer getParam(String name)
				{
					String pre = impl + "-http-response-";
					String s   = name.substring(pre.length());
					int    idx = s.indexOf(".");
					s          = s.substring(0, idx);
					return Integer.parseInt(s);
				}

				@Override
				public String getName(Integer i)
				{
					return impl + "-http-response-" + i + ".html";
				}
			});
			return container.next();
		}
		return File.createTempFile(impl + "-http-response-", ".html");
	}
}
