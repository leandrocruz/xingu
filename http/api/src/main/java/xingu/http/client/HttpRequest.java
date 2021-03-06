package xingu.http.client;

import java.util.List;
import java.util.Map;

import org.jboss.netty.handler.codec.http.Cookie;

public interface HttpRequest
{
	String getUri();

	HttpRequest context(HttpContext ctx);
	HttpContext getContext();

	String getMethod();
	boolean isPost();

	List<NameValue> getHeaders();
	HttpRequest header(String name, String value);

	List<NameValue> getFields();
	HttpRequest field(String nameValue);
	HttpRequest field(String name, String value);
	HttpRequest field(String name, String value, String type);
	HttpRequest field(String name, int value);
	HttpRequest field(String name, long value);
	HttpRequest fields(Map<String, String> map);
	
	//HttpRequest queryString(String name, String value);

	Cookies getCookies();
	HttpRequest withCookie(Cookie cookie);
	HttpRequest withCookies(Cookies cookies);

	HttpRequest multipart(boolean isMultipartFormData);
	boolean isMultipart();
	
	String getProxy();
	HttpRequest withProxy(String proxy);

	String getCertificate();
	HttpRequest withCertificate(String certificate);
	HttpRequest withCertificate(String certificate, String password);
	String getCertificatePassword();

	String getUserAgent();
	HttpRequest withUserAgent(String ua);

	List<Attachment> getAttachments();
	HttpRequest withAttachment(Attachment attachment);	

	String getAuthenticationUser();
	String getAuthenticationPassword();
	HttpRequest withAuthentication(String user, String password);

	HttpResponse exec()
		throws HttpException;

	HttpResponse execAndRetry(int attempts)
		throws HttpException;

	HttpRequest expects(int code);
	HttpRequest expects(int code, String messageIfError);
	HttpRequest expects(ResponseInspector inspector);

	void setCharset(String charset);
	String getCharset();

	HttpRequest name(String name);

	HttpRequest ignoreSSLCertificates(boolean ignore);
	boolean ignoreSSLCertificates();

	HttpRequest sslAllowBeast(boolean allow);
	boolean sslAllowBeast();

	HttpRequest sslV3(boolean v3);
	boolean sslV3();

	HttpRequest listener(HttpProgressListener listener);

	HttpRequest payload(String payload);
	String getPayload();
	
	HttpRequest payloadAsJson(String payload);
	String getPayloadAsJson();

	boolean isSoap();
	HttpRequest soap(boolean isSoap);

	String getKeepAlive();
	HttpRequest withKeepAlive(String seconds);
	
	boolean hasPayload();
	HttpRequest withoutPayload();
}