package xingu.http.client;

public interface HttpClient
{
	HttpRequest get(String uri)
		throws HttpException;

	HttpRequest post(String uri)
		throws HttpException;
	
	HttpRequest put(String uri)
		throws HttpException;
	
	HttpRequest delete(String uri)
		throws HttpException;
	
	HttpRequest options(String uri)
		throws HttpException;
	
	HttpRequest head(String uri)
		throws HttpException;

	Cookies getCookies(String uri)
		throws HttpException;
}
