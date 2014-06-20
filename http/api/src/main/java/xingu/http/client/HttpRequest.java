package xingu.http.client;

import java.util.List;

import org.jboss.netty.handler.codec.http.Cookie;

public interface HttpRequest
{
	String getUri();

	HttpRequest header(String name, String value);

	HttpRequest field(String nameValue);
	
	HttpRequest field(String name, String value);
	
	HttpRequest field(String name, int value);
	
	HttpRequest field(String name, long value);
	
	HttpRequest queryString(String name, String value);

	HttpRequest withCertificate(String certificate);

	HttpRequest withCookie(Cookie cookie);

	HttpResponse exec()
		throws HttpException;

	String getCertificate();

	List<Cookie> getCookies();

	List<NameValue> getFields();

	boolean isPost();

	boolean isMultipart();
	
	List<NameValue> getHeaders();

	HttpRequest withCertificate(String certificate, String password);

	String getCertificatePassword();

	HttpRequest withCookies(Cookies cookies);

	HttpRequest withUserAgent(String ua);

	String getUserAgent();
	
	HttpRequest multipart(boolean isMultipartFormData);

	HttpRequest upload(String name, String filePath);

	List<NameValue> getUploadFiles();
	
	HttpRequest ndc(String ndc);

	HttpRequest withAuthentication(String user, String password);

	String getAuthenticationUser();

	String getAuthenticationPassword();
}
