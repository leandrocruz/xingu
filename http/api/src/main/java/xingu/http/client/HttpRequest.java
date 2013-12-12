package xingu.http.client;

public interface HttpRequest
{
	HttpRequest header(String name, String value);

	HttpRequest field(String name, String value);

	HttpResponse<String> asString()
		throws HttpException;

	HttpRequest withCertificate(String certificate);

	String getUri();

}
