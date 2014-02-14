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

	private boolean				isPost;

	private String				uri;

	private String				ua;

	private String				certificate;

	private String				certificatePassword;
	
	private String				authUser;
	
	private String				authPassword;

	private boolean				multipart;

	private String				ndc;

	private List<Cookie>		cookies	= new ArrayList<Cookie>();

	private List<NameValue>		fields	= new ArrayList<NameValue>();

	private List<NameValue>		upload	= new ArrayList<NameValue>();

	private List<NameValue>		headers	= new ArrayList<NameValue>();

	private static final Logger	logger	= LoggerFactory.getLogger(SimpleHttpRequest.class);

	public SimpleHttpRequest(String uri, boolean isPost)
	{
		this.uri = uri;
		this.isPost = isPost;
	}

	@Override
	public String getUri()
	{
		return uri;
	}

	@Override
	public HttpRequest header(String name, String value)
	{
		headers.add(new NameValueImpl(name, value));
		return this;
	}

	@Override
	public HttpRequest field(String name, String value)
	{
		fields.add(new NameValueImpl(name, value));
		return this;
	}

	@Override
	public HttpRequest withCertificate(String certificate)
	{
		this.certificate = certificate;
		return this;
	}

	@Override
	public HttpRequest withCookie(Cookie cookie)
	{
		this.cookies.add(cookie);
		return this;
	}
	@Override
	public String getCertificate()
	{
		return certificate;
	}

	@Override
	public List<NameValue> getFields()
	{
		return fields;
	}
	
	@Override
	public List<NameValue> getHeaders()
	{
		return headers;
	}
	
	@Override
	public List<NameValue> getUploadFiles()
	{
		return upload;
	}

	@Override
	public List<Cookie> getCookies()
	{
		return cookies;
	}

	@Override
	public boolean isPost()
	{
		return isPost;
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

	@Override
	public HttpRequest withCertificate(String certificate, String password)
	{
		this.certificatePassword = password;
		return withCertificate(certificate);
	}

	@Override
	public String getCertificatePassword()
	{
		return certificatePassword;
	}

	@Override
	public HttpRequest withCookies(Cookies c)
	{
		cookies.addAll(c.set());
		return this;
	}
	
	@Override
	public HttpRequest withUserAgent(String ua)
	{
		this.ua = ua;
		return this;
	}
	
	public HttpRequest multipart(boolean multipart)
	{
		this.multipart = multipart;
		return this;
	}

	@Override
	public HttpRequest upload(String name, String filePath)
	{
		upload.add(new NameValueImpl(name, filePath));
		return this;
	}
	
	@Override
	public HttpRequest withAuthentication(String user, String password)
	{
		this.authUser 		= user;
		this.authPassword 	= password;
		return this;
	}
	
	@Override
	public String getAuthenticationUser()
	{
		return authUser;
	}

	@Override
	public String getAuthenticationPassword()
	{
		return authPassword;
	}
	
	@Override
	public String getUserAgent()
	{
		return ua;
	}
	
	public boolean isMultipart()
	{
		return multipart;
	}

	@Override
	public HttpRequest ndc(String ndc)
	{
		this.ndc = ndc;
		return this;
	}

	@Override
	public HttpRequest queryString(String name, String filePath)
	{
		throw new NotImplementedYet();
	}
}
