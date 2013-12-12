package xingu.http.client.impl.wget;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import xingu.http.client.Header;
import xingu.http.client.HttpException;
import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;
import xingu.http.client.impl.HeaderImpl;
import xingu.process.ProcessManager;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.NumberUtils;

public class WgetRequest
	implements HttpRequest
{
	@Inject
	private ProcessManager pm;

	private String	uri;

	private String	certificate;

	public WgetRequest(String uri)
	{
		this.uri = uri;
	}

	@Override
	public HttpRequest header(String name, String value)
	{
		return null;
	}

	@Override
	public HttpRequest field(String name, String value)
	{
		return null;
	}

	@Override
	public HttpResponse<String> asString()
		throws HttpException
	{
		try
		{
			File                  file   = File.createTempFile("wget-http-client-", ".html");
			String                cmd    = buildLine(file, certificate, uri);
			ByteArrayOutputStream os     = new ByteArrayOutputStream();
			int                   result = pm.exec(cmd, new File("/tmp"), os);
			if(result != 0)
			{
				throw new NotImplementedYet("ERROR executing wget processes: " + result);
			}
			
			return WgetResponseBuilder.buildFrom(uri, file, os.toByteArray());
		}
		catch(Exception e)
		{
			throw new HttpException(e);
		}
	}

	private String buildLine(File file, String certificate, String uri)
	{
		String[] args = {
				"wget -q -S",
				"-O", 				file.toString(),
				"--certificate", 	certificate,
				uri
		};
		return StringUtils.join(args, " ");
	}

	@Override
	public String getUri()
	{
		return uri;
	}

	@Override
	public HttpRequest withCertificate(String certificate)
	{
		this.certificate = certificate;
		return this;
	}
}
