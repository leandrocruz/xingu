package xingu.http.client;

import java.io.InputStream;
import java.util.List;

import org.jboss.netty.handler.codec.http.Cookie;

public interface HttpRequest
{
	String getUri();

	HttpRequest header(String name, String value);

	HttpRequest field(String name, String value);
	
	HttpRequest queryString(String name, String value);

	HttpRequest withCertificate(String certificate);

	HttpRequest withCookie(Cookie cookie);

	HttpResponse<String> asString()
		throws HttpException;

	HttpResponse<InputStream> asData()
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
}
